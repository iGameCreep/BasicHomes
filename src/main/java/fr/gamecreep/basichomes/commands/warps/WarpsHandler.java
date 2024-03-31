package fr.gamecreep.basichomes.commands.warps;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.commands.DisabledCommand;
import fr.gamecreep.basichomes.entities.enums.ConfigElement;
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

    public WarpsHandler(BasicHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (!((boolean) this.plugin.getPluginConfig().getConfig().get(ConfigElement.WARPS_ENABLED))) {
            return new DisabledCommand(this.plugin, PositionType.WARP).onCommand(commandSender);
        }

        return switch (command.getName()) {
            case "setwarp" -> new CreateWarp(this.plugin).onCommand(commandSender, args);
            case "delwarp" -> new DeleteWarp(this.plugin).onCommand(commandSender, args);
            case "warps" -> new GetWarps(this.plugin).onCommand(commandSender);
            case "warp" -> new TeleportWarp(this.plugin).onCommand(commandSender, args);
            default -> false;
        };
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        return switch (command.getName()) {
            case "setwarp" -> new CreateWarp(this.plugin).onTabComplete();
            case "delwarp" -> new DeleteWarp(this.plugin).onTabComplete(commandSender);
            case "warp" -> new TeleportWarp(this.plugin).onTabComplete(commandSender);
            default -> Collections.emptyList();
        };
    }
}
