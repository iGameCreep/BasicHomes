package fr.gamecreep.basichomes.commands.create;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.classes.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.files.DataHandler;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public abstract class CreateCommand implements CommandExecutor, TabCompleter {
    private final BasicHomes plugin;
    private final PositionType type;
    private final Permission permission;
    private final DataHandler handler;

    protected CreateCommand(BasicHomes plugin, PositionType type, Permission permission) {
        this.plugin = plugin;
        this.type = type;
        this.permission = permission;
        if (type == PositionType.HOME) this.handler = plugin.getHomeHandler();
        else this.handler = plugin.getWarpHandler();
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (commandSender instanceof Player playerSender) {
            if (!playerSender.hasPermission(this.permission.getName())) {
                this.plugin.getChatUtils().sendNoPermission(playerSender, this.permission);
                return true;
            }

            if (args.length == 0) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, String.format("Please add a %s name !", this.type.getDisplayName()));
                return true;
            }

            Location playerPos = playerSender.getLocation();
            String name = args[0];

            List<SavedPosition> list = this.handler.getAllByPlayer(playerSender);
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

    @Override
    public List<String> onTabComplete(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        return Collections.singletonList("[name]");
    }
}
