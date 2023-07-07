package fr.gamecreep.basichomes.entities.commands;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.homes.PlayerHome;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetHome implements CommandExecutor {
    private final BasicHomes plugin;

    public SetHome(BasicHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {

        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;
            Location playerPos = playerSender.getLocation();

            if (args.length < 1) {
                plugin.getChatUtils().sendPlayerError(playerSender, "Please add a home name !");
                return false;
            }

            List<PlayerHome> playerHomeList = plugin.getAllPlayerHomes(playerSender);
            for (PlayerHome home : playerHomeList) {
                if (home.getHomeName().equalsIgnoreCase(args[0])) {
                    plugin.getChatUtils().sendPlayerError(playerSender, "A home with this name already exists !");
                    return true;
                }
            }

            plugin.createHome(args[0], playerSender, playerPos);
            plugin.getChatUtils().sendPlayerInfo(playerSender, "Home §e" + args[0] + "§b has been created !");

            return true;
        }

        return false;
    }
}
