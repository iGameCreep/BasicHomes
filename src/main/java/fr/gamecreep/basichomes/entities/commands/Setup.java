package fr.gamecreep.basichomes.entities.commands;

import fr.gamecreep.basichomes.BasicHomes;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Setup implements CommandExecutor  {
    private final BasicHomes plugin;

    public Setup(BasicHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

            plugin.buildDatabase();
            plugin.getChatUtils().sendPlayerInfo(playerSender, "Database has been built ! Using this command again will reset all player homes registered.");
            return true;
        }
        return false;
    }
}
