package fr.gamecreep.basichomes.commands.teleport;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.files.PositionDataHandler;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TeleportCommand implements CommandExecutor, TabCompleter {
    private final BasicHomes plugin;
    private final PositionType type;
    private final Permission permission;
    private final PositionDataHandler handler;

    protected TeleportCommand(BasicHomes plugin, PositionType type, Permission permission) {
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
                this.plugin.getChatUtils().sendPlayerError(playerSender, String.format("Please add the name of the %s to teleport to !", this.type.getDisplayName()));
                return true;
            }

            String name = args[0];
            SavedPosition pos = this.type == PositionType.HOME ? this.handler.getByName(playerSender, name) : this.handler.getByName(name);

            if (pos == null) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, String.format("No %s exists with that name !", this.type.getDisplayName()));
                return true;
            }

            this.plugin.getTeleportUtils().add(playerSender, pos);

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (commandSender instanceof Player playerSender) {
            List<String> homeNameList = new ArrayList<>();
            List<SavedPosition> list = this.type == PositionType.HOME ? this.handler.getAllByPlayer(playerSender) : this.handler.getAll();

            for (SavedPosition pos : list) {
                homeNameList.add(pos.getName());
            }
            return homeNameList;
        }
        return Collections.emptyList();
    }
}
