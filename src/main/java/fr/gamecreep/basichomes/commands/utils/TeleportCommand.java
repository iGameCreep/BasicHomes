package fr.gamecreep.basichomes.commands.utils;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.files.PositionDataHandler;
import fr.gamecreep.basichomes.utils.ChatUtils;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TeleportCommand {
    private final BasicHomes plugin;
    private final PositionType type;
    private final Permission permission;
    private final PositionDataHandler handler;

    protected TeleportCommand(BasicHomes plugin, PositionType type, Permission permission) {
        this.plugin = plugin;
        this.type = type;
        this.permission = permission;
        this.handler = plugin.getPositionDataHandler();
    }

    public boolean onCommand(@NonNull final CommandSender commandSender, @NonNull final String[] args) {
        if (commandSender instanceof final Player playerSender) {
            if (!playerSender.hasPermission(this.permission.getName())) {
                ChatUtils.sendNoPermission(playerSender, this.permission);
                return true;
            }

            if (args.length < 1) {
                ChatUtils.sendPlayerError(playerSender, String.format("Please add the name of the %s to teleport to !", this.type.getDisplayName()));
                return true;
            }

            final String name = args[0];
            final SavedPosition pos = this.handler.getByName(this.type, playerSender, name);

            if (pos == null) {
                ChatUtils.sendPlayerError(playerSender, String.format("No %s exists with that name !", this.type.getDisplayName()));
                return true;
            }

            this.plugin.getTeleportUtils().add(playerSender, pos);

            return true;
        }

        return false;
    }

    public List<String> onTabComplete(@NonNull final CommandSender commandSender, @NonNull final String[] args) {
        if (commandSender instanceof final Player playerSender) {
            final List<String> nameList = new ArrayList<>();
            final List<SavedPosition> list = this.handler.getAllByPlayer(this.type, playerSender);

            for (final SavedPosition pos : list) {
                if (pos.getName().contains(args[0])) {
                    nameList.add(pos.getName());
                }
            }

            return nameList;
        }
        return Collections.emptyList();
    }
}
