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
    private final CreateHome createHome;
    private final DeleteHome deleteHome;
    private final DeleteHomeOf deleteHomeOf;
    private final EditHome editHome;
    private final GetHomes getHomes;
    private final GetHomesOf getHomesOf;
    private final TeleportHome teleportHome;

    public HomesHandler(BasicHomes plugin) {
        this.plugin = plugin;
        this.createHome = new CreateHome(plugin);
        this.deleteHome = new DeleteHome(plugin);
        this.deleteHomeOf = new DeleteHomeOf(plugin);
        this.editHome = new EditHome(plugin);
        this.getHomes = new GetHomes(plugin);
        this.getHomesOf = new GetHomesOf(plugin);
        this.teleportHome = new TeleportHome(plugin);
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (!((boolean) this.plugin.getPluginConfig().getConfig().get(ConfigElement.HOMES_ENABLED))) {
            return new DisabledCommand(PositionType.HOME).onCommand(commandSender);
        }

        return switch (command.getName()) {
            case "sethome" -> this.createHome.onCommand(commandSender, args);
            case "delhome" -> this.deleteHome.onCommand(commandSender, args);
            case "delhomeof" -> this.deleteHomeOf.onCommand(commandSender, args);
            case "edithome" -> this.editHome.onCommand(commandSender, args);
            case "homes" -> this.getHomes.onCommand(commandSender);
            case "homesof" -> this.getHomesOf.onCommand(commandSender, args);
            case "home" -> this.teleportHome.onCommand(commandSender, args);
            default -> false;
        };
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        return switch (command.getName()) {
            case "sethome" -> this.createHome.onTabComplete(commandSender, args);
            case "delhome" -> this.deleteHome.onTabComplete(commandSender, args);
            case "delhomeof" -> this.deleteHomeOf.onTabComplete(commandSender, args);
            case "edithome" -> this.editHome.onTabComplete(commandSender, args);
            case "homesof" -> this.getHomesOf.onTabComplete(commandSender, args);
            case "home" -> this.teleportHome.onTabComplete(commandSender, args);
            default -> Collections.emptyList();
        };
    }
}
