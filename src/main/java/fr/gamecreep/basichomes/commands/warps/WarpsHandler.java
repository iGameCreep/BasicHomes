package fr.gamecreep.basichomes.commands.warps;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.commands.utils.DisabledCommand;
import fr.gamecreep.basichomes.config.enums.ConfigElement;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;

public class WarpsHandler implements CommandExecutor, TabCompleter {
    private final BasicHomes plugin;
    private final CreateWarp createWarp;
    private final DeleteWarp deleteWarp;
    private final EditWarp editWarp;
    private final GetWarps getWarps;
    private final TeleportWarp teleportWarp;

    public WarpsHandler(BasicHomes plugin) {
        this.plugin = plugin;
        this.createWarp = new CreateWarp(plugin);
        this.deleteWarp = new DeleteWarp(plugin);
        this.editWarp = new EditWarp(plugin);
        this.getWarps = new GetWarps(plugin);
        this.teleportWarp = new TeleportWarp(plugin);
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (!((boolean) this.plugin.getPluginConfig().getConfig().get(ConfigElement.WARPS_ENABLED))) {
            return new DisabledCommand(PositionType.WARP).onCommand(commandSender);
        }

        return switch (command.getName()) {
            case "setwarp" -> this.createWarp.onCommand(commandSender, args);
            case "delwarp" -> this.deleteWarp.onCommand(commandSender, args);
            case "editwarp" -> this.editWarp.onCommand(commandSender, args);
            case "warps" -> this.getWarps.onCommand(commandSender);
            case "warp" -> this.teleportWarp.onCommand(commandSender, args);
            default -> false;
        };
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        return switch (command.getName()) {
            case "setwarp" -> this.createWarp.onTabComplete(commandSender, args);
            case "delwarp" -> this.deleteWarp.onTabComplete(commandSender, args);
            case "editwarp" -> this.editWarp.onTabComplete(commandSender, args);
            case "warp" -> this.teleportWarp.onTabComplete(commandSender, args);
            default -> Collections.emptyList();
        };
    }
}
