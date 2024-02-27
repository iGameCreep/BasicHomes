package fr.gamecreep.basichomes.entities.commands.get;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.menus.WarpMenu;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Warps implements CommandExecutor {
    private final BasicHomes plugin;

    public Warps(BasicHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {

        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

            int currentPage = 1;
            WarpMenu menu = new WarpMenu(this.plugin);
            menu.openInventory(playerSender, currentPage);

            return true;
        }

        return false;
    }

}
