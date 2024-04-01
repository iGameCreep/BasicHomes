package fr.gamecreep.basichomes.commands.utils;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.files.PositionDataHandler;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DeleteCommand {
    private final BasicHomes plugin;
    private final PositionType type;
    private final Permission permission;
    private final PositionDataHandler handler;

    protected DeleteCommand(BasicHomes plugin, PositionType type, Permission permission) {
        this.plugin = plugin;
        this.type = type;
        this.permission = permission;
        if (type == PositionType.HOME) this.handler = plugin.getHomeHandler();
        else this.handler = plugin.getWarpHandler();
    }

    public boolean onCommand(@NonNull final CommandSender commandSender, @NonNull final String[] args) {
        if (commandSender instanceof Player playerSender) {
            if (!playerSender.hasPermission(this.permission.getName())) {
                this.plugin.getChatUtils().sendNoPermission(playerSender, this.permission);
                return true;
            }

            if (args.length < 1) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, String.format("Please add the name of the %s to delete !", this.type.getDisplayName()));
                return false;
            }

            String name = args[0];
            SavedPosition pos = this.handler.getByName(playerSender, name);

            if (pos == null) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, String.format("No %s exists with that name !", this.type.getDisplayName()));
                return true;
            }

            this.handler.delete(pos);

            this.plugin.getChatUtils().sendPlayerInfo(playerSender, String.format(
                    "The %s %s%s%s has been removed !",
                    this.type.getDisplayName(),
                    Constants.SPECIAL_COLOR,
                    name,
                    Constants.SUCCESS_COLOR
            ));

            return true;
        }

        return false;
    }

    public List<String> onTabComplete(@NonNull final CommandSender commandSender, @NonNull final String[] args) {
        if (commandSender instanceof Player playerSender) {
            List<String> nameList = new ArrayList<>();

            if (args.length == 1) {
                List<SavedPosition> list = this.type == PositionType.HOME ? this.handler.getAllByPlayer(playerSender) : this.handler.getAll();

                for (SavedPosition pos : list) {
                    if (pos.getName().contains(args[0])) {
                        nameList.add(pos.getName());
                    }
                }
            }

            return nameList;
        }

        return Collections.emptyList();
    }
}
