package fr.gamecreep.basichomes.menus.tools;

import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.SavedPosition;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PaginatedMenuFactory<MENU extends PaginatedMenu> {

    private final Set<MENU> openedMenus = new HashSet<>();

    protected PaginatedMenuFactory() { }

    public final void openInventory(@NonNull final Player player, @NonNull final MENU menu, @NonNull final List<SavedPosition> data) {
        menu.populateMenu(data);
        this.openedMenus.add(menu);
        player.openInventory(menu.getMenu());
    }

    protected void onInventoryClose(@NonNull final InventoryCloseEvent event) {
        final MENU menu = this.getMenuFromInventory(event.getView().getTopInventory());
        if (menu == null) return;

        this.openedMenus.remove(menu);
    }

    protected void onInventoryClick(@NonNull final InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        final Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) return;

        final MENU menu = this.getMenuFromInventory(clickedInventory);
        if (menu == null) return;

        event.setCancelled(true);

        final ItemStack item = event.getCurrentItem();
        if (item == null) return;

        final ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return;

        if (item.getType().equals(Constants.PAGE_INDICATOR_ITEM)) {
            menu.refreshMenu();
            return;
        } else if (itemMeta.getDisplayName().equals(Constants.PREVIOUS_PAGE_ITEM_NAME)) {
            menu.pageDown();
            return;
        } else if (itemMeta.getDisplayName().equals(Constants.NEXT_PAGE_ITEM_NAME)) {
            menu.pageUp();
            return;
        }

        menu.onMenuClickEvent(event);
    }

    private MENU getMenuFromInventory(@NonNull final Inventory inv) {
        for (final MENU menu : this.openedMenus) {
            if (menu.getMenu().equals(inv)) return menu;
        }

        return null;
    }
}
