package fr.gamecreep.basichomes.entities.commands;

import fr.gamecreep.basichomes.BasicHomes;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Homes implements CommandExecutor {
    private final BasicHomes plugin;

    public Homes(BasicHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

            int currentPage = 1;
            plugin.getHomesUtils().openHomeInventory(playerSender, currentPage);
            return true;
        }

        return false;
    }

}
