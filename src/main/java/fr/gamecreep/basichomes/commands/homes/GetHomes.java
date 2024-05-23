package fr.gamecreep.basichomes.commands.homes;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.menus.home.HomeMenu;
import fr.gamecreep.basichomes.menus.home.HomeMenuFactory;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GetHomes {

    private final BasicHomes plugin;

    public GetHomes(final BasicHomes plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NonNull final CommandSender commandSender) {
        if (commandSender instanceof Player playerSender) {
            final HomeMenuFactory factory = this.plugin.getHomeMenuFactory();
            final List<SavedPosition> homes = this.plugin.getPositionDataHandler().getAllByPlayer(PositionType.HOME, playerSender);

            factory.openInventory(playerSender, new HomeMenu(this.plugin, playerSender, PositionType.HOME), homes);
            return true;
        }

        return false;
    }
}
