package fr.gamecreep.basichomes.commands.utils;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.config.enums.ConfigElement;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.files.PositionDataHandler;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public abstract class CreateCommand {
    private final BasicHomes plugin;
    private final PositionType type;
    private final Permission permission;
    private final PositionDataHandler handler;

    protected CreateCommand(@NonNull final BasicHomes plugin, final PositionType type, final Permission permission) {
        this.plugin = plugin;
        this.type = type;
        this.permission = permission;
        this.handler = plugin.getPositionDataHandler();
    }

    public boolean onCommand(@NonNull final CommandSender commandSender, @NonNull final String[] args) {
        if (commandSender instanceof final Player playerSender) {
            if (!playerSender.hasPermission(this.permission.getName())) {
                this.plugin.getChatUtils().sendNoPermission(playerSender, this.permission);
                return true;
            }

            if (args.length < 1) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, "Please add a name !");
                return true;
            }

            final List<SavedPosition> list = this.handler.getAllByPlayer(this.type, playerSender);

            if (this.type.equals(PositionType.HOME) && !this.canCreateHome(playerSender, list.size())) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, "You already have the max number of homes allowed !");
                return true;

            }

            final Location playerPos = playerSender.getLocation();
            final String name = args[0];

            for (final SavedPosition pos : list) {
                if (pos.getName().equalsIgnoreCase(name)) {
                    this.plugin.getChatUtils().sendPlayerError(playerSender, String.format("A %s with this name already exists !", this.type.getDisplayName()));
                    return true;
                }
            }

            this.handler.create(new SavedPosition(name, playerSender.getUniqueId().toString(), playerPos, this.type));
            this.plugin.getChatUtils().sendPlayerInfo(playerSender, String.format(
                    "The %s %s%s%s has been created !",
                    this.type.getDisplayName(),
                    Constants.SPECIAL_COLOR,
                    name,
                    Constants.SUCCESS_COLOR
            ));

            return true;
        }

        return false;
    }

    public List<String> onTabComplete() {
        return Collections.singletonList("[name]");
    }

    private boolean canCreateHome(@NonNull final Player player, final int currentHomes) {
        final boolean opBypassLimit = (boolean) this.plugin.getPluginConfig().getConfig().get(ConfigElement.OP_BYPASS_HOME_LIMIT);
        if (opBypassLimit && player.hasPermission("op")) return true;

        final int maxHomes = (int) this.plugin.getPluginConfig().getConfig().get(ConfigElement.MAX_HOMES);
        if (maxHomes == 0) return true;

        return currentHomes < maxHomes;
    }
}
