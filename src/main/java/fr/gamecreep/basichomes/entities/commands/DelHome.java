package fr.gamecreep.basichomes.entities.commands;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.homes.PlayerHome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DelHome implements CommandExecutor, TabCompleter {
    private final BasicHomes plugin;

    public DelHome(BasicHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

            if (args.length < 1) {
                plugin.getChatUtils().sendPlayerError(playerSender, "Please add the name of the home to delete !");
                return false;
            }
            try {
                plugin.removeHomeByName(playerSender, args[0]);
            } catch (Error err) {
                plugin.getChatUtils().sendPlayerError(playerSender, "No home exists with that name !");
                return true;
            }

            plugin.getChatUtils().sendPlayerInfo(playerSender, "Home §e" + args[0] + "§b has been removed !");
            return true;
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

            List<String> homeNameList = new ArrayList<>();
            List<PlayerHome> homeList = plugin.getAllPlayerHomes(playerSender);

            for (PlayerHome home : homeList) {
                homeNameList.add(home.getHomeName());
            }
            return homeNameList;
        }
        return null;
    }
}