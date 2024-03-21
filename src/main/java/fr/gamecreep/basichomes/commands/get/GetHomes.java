package fr.gamecreep.basichomes.commands.get;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.menus.home.HomeMenu;
import fr.gamecreep.basichomes.menus.home.HomeMenuFactory;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GetHomes implements CommandExecutor {

    private final BasicHomes plugin;

    public GetHomes(BasicHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull final CommandSender commandSender, @NonNull final Command command, @NonNull final String label, @NonNull final String[] args) {
        if (commandSender instanceof Player playerSender) {
            HomeMenuFactory factory = this.plugin.getHomeMenuFactory();
            List<SavedPosition> homes = this.plugin.getHomeHandler().getAllByPlayer(playerSender);
            factory.openInventory(playerSender, new HomeMenu(this.plugin, playerSender), homes);

            return true;
        }

        return false;
    }
}
