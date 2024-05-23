package fr.gamecreep.basichomes.commands.warps;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.menus.warp.WarpMenu;
import fr.gamecreep.basichomes.menus.warp.WarpMenuFactory;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GetWarps {

    private final BasicHomes plugin;

    public GetWarps(BasicHomes plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(@NonNull final CommandSender commandSender) {
        if (commandSender instanceof Player playerSender) {
            final WarpMenuFactory factory = this.plugin.getWarpMenuFactory();
            final List<SavedPosition> warps = this.plugin.getPositionDataHandler().getAll(PositionType.WARP);

            factory.openInventory(playerSender, new WarpMenu(this.plugin, playerSender), warps);

            return true;
        }

        return false;
    }
}
