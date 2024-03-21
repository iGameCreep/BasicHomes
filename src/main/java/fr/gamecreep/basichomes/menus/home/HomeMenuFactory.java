package fr.gamecreep.basichomes.menus.home;

import fr.gamecreep.basichomes.menus.tools.PaginatedMenuFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class HomeMenuFactory extends PaginatedMenuFactory<HomeMenu> implements Listener {

    @EventHandler
    private void onInventoryClickEvent(InventoryClickEvent e) {
        super.onInventoryClick(e);
    }

    @EventHandler
    private void onInventoryCloseEvent(InventoryCloseEvent e) {
        super.onInventoryClose(e);
    }
}
