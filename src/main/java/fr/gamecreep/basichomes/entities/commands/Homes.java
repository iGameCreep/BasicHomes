package fr.gamecreep.basichomes.entities.commands;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.homes.PlayerHome;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Homes implements CommandExecutor {
    private final BasicHomes plugin;

    public Homes(BasicHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

            try {
                List<PlayerHome> playerHomeList = plugin.getAllPlayerHomes(playerSender);
                StringBuilder str = new StringBuilder();

                for (PlayerHome home : playerHomeList) {
                    str.append(home.getHomeName() + " ");
                }

                plugin.getChatUtils().sendPlayerInfo(playerSender, "Here is a list of your homes !\n" + str);
                return true;
            } catch (Throwable err) {
                plugin.getChatUtils().sendPlayerError(playerSender, "Could not get your homes. Please try again later or call administrators if this error still occurs.");
                throw new RuntimeException(err);
            }
        }

        return false;
    }
}
