package fr.gamecreep.basichomes.commands.homes;

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

public class HomesHandler implements CommandExecutor, TabCompleter {
    private final BasicHomes plugin;

    public HomesHandler(BasicHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (!((boolean) this.plugin.getPluginConfig().getConfig().get(ConfigElement.HOMES_ENABLED))) {
            return new DisabledCommand(this.plugin, PositionType.HOME).onCommand(commandSender);
        }

        return switch (command.getName()) {
            case "sethome" -> new CreateHome(this.plugin).onCommand(commandSender, args);
            case "delhome" -> new DeleteHome(this.plugin).onCommand(commandSender, args);
            case "delhomeof" -> new DeleteHomeOf(this.plugin).onCommand(commandSender, args);
            case "homes" -> new GetHomes(this.plugin).onCommand(commandSender);
            case "homesof" -> new GetHomesOf(this.plugin).onCommand(commandSender, args);
            case "home" -> new TeleportHome(this.plugin).onCommand(commandSender, args);
            default -> false;
        };
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        return switch (command.getName()) {
            case "sethome" -> new CreateHome(this.plugin).onTabComplete();
            case "delhome" -> new DeleteHome(this.plugin).onTabComplete(commandSender, args);
            case "delhomeof" -> new DeleteHomeOf(this.plugin).onTabComplete(commandSender, args);
            case "homesof" -> new GetHomesOf(this.plugin).onTabComplete(commandSender, args);
            case "home" -> new TeleportHome(this.plugin).onTabComplete(commandSender, args);
            default -> Collections.emptyList();
        };
    }
}
