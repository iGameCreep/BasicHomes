package fr.gamecreep.basichomes.commands.utils;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.ConfigElement;
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

    protected CreateCommand(BasicHomes plugin, PositionType type, Permission permission) {
        this.plugin = plugin;
        this.type = type;
        this.permission = permission;
        if (type == PositionType.HOME) this.handler = plugin.getHomeHandler();
        else this.handler = plugin.getWarpHandler();
    }

    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull String[] args) {
        if (commandSender instanceof Player playerSender) {
            if (!playerSender.hasPermission(this.permission.getName())) {
                this.plugin.getChatUtils().sendNoPermission(playerSender, this.permission);
                return true;
            }

            List<SavedPosition> list = this.handler.getAllByPlayer(playerSender);

            if (this.type == PositionType.HOME && !this.canCreateHome(playerSender, list.size())) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, "You already have the max number of homes allowed !");
                return true;

            }

            if (args.length == 0) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, String.format("Please add a %s name !", this.type.getDisplayName()));
                return true;
            }

            Location playerPos = playerSender.getLocation();
            String name = args[0];

            for (SavedPosition pos : list) {
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

    private boolean canCreateHome(@NonNull Player player, int currentHomes) {
        boolean opBypassLimit = (boolean) this.plugin.getPluginConfig().getConfig().get(ConfigElement.OP_BYPASS_HOME_LIMIT);
        if (opBypassLimit && player.hasPermission("op")) return true;
        int maxHomes = (int) this.plugin.getPluginConfig().getConfig().get(ConfigElement.MAX_HOMES);
        return currentHomes < maxHomes;
    }
}
