package fr.gamecreep.basichomes.entities.commands;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.homes.PlayerHome;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Home implements CommandExecutor, TabCompleter {
    private final BasicHomes plugin;

    public Home(BasicHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

            if (args.length == 0) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, "Please add the name of the home to teleport to !");
                return true;
            }

            String homeName = args[0];
            PlayerHome home = this.plugin.getHomeByName(playerSender, homeName);

            if (home == null) {
                this.plugin.getChatUtils().sendPlayerError(playerSender, "No home exists with that name !");
                return true;
            }

            Location location = home.getLocation();
            location.setPitch(playerSender.getLocation().getPitch());
            location.setYaw(playerSender.getLocation().getYaw());
            playerSender.teleport(location);

            this.plugin.getChatUtils().sendPlayerInfo(playerSender, String.format("Teleporting you to %s%s%s...", Constants.SPECIAL_COLOR, homeName, Constants.SUCCESS_COLOR));

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String s, @NonNull String[] strings) {
        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

            List<String> homeNameList = new ArrayList<>();
            List<PlayerHome> homeList = this.plugin.getAllPlayerHomes(playerSender);

            for (PlayerHome home : homeList) {
                homeNameList.add(home.getName());
            }
            return homeNameList;
        }
        return Collections.emptyList();
    }
}
