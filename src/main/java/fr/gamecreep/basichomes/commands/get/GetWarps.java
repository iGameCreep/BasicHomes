package fr.gamecreep.basichomes.commands.get;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.classes.SavedPosition;
import fr.gamecreep.basichomes.menus.warp.WarpMenu;
import fr.gamecreep.basichomes.menus.warp.WarpMenuFactory;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GetWarps implements CommandExecutor {

    private final BasicHomes plugin;

    public GetWarps(BasicHomes plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull final CommandSender commandSender, @NonNull final Command command, @NonNull final String label, @NonNull final String[] args) {
        if (commandSender instanceof Player playerSender) {
            WarpMenuFactory factory = this.plugin.getWarpMenuFactory();
            List<SavedPosition> warps = this.plugin.getWarpHandler().getAll();
            factory.openInventory(playerSender, new WarpMenu(this.plugin, playerSender), warps);

            return true;
        }

        return false;
    }
}
