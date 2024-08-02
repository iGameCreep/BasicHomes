package fr.gamecreep.basichomes.menus.tools;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.exceptions.BasicHomesException;
import fr.gamecreep.basichomes.utils.ChatUtils;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

@Getter
public abstract class PaginatedMenu {

    private final BasicHomes plugin;
    private final Inventory menu;
    private final Player player;
    private int page = 1;
    private final Permission permissionToDelete;
    private List<SavedPosition> data = Collections.emptyList();

    protected PaginatedMenu(@NonNull final BasicHomes plugin,
                            @NonNull final Player player,
                            @NonNull final Player target,
                            final String menuName,
                            final Permission permissionToDelete) {
        this.plugin = plugin;
        this.menu = Bukkit.createInventory(target, 54, menuName);
        this.player = player;
        this.permissionToDelete = permissionToDelete;
    }

    protected abstract void onMenuClickEvent(@NonNull final InventoryClickEvent inventoryClickEvent);

    protected final void pageUp() {
        this.page += 1;
        this.refreshMenu();
    }

    protected final void pageDown() {
        this.page -= 1;
        this.refreshMenu();
    }

    protected final void refreshMenu() {
        this.menu.clear();
        this.populateMenu(this.data);
    }

    protected final void populateMenu(final List<SavedPosition> data) {
        try {
            this.data = data;
            final int itemsPerPage = 4;
            final int totalItems = data.size();
            int totalPages = totalItems / itemsPerPage;
            if (totalItems % itemsPerPage != 0) totalPages++;

            addPaginationButtons(totalPages);

            final int startIndex = (this.page - 1) * itemsPerPage;
            final int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

            for (int i = startIndex; i < endIndex; i++) {
                final SavedPosition pos = data.get(i);

                final ItemStack item = createItem(pos);
                final int homeItemSlot = 10 + ((i - startIndex) * 9);
                this.menu.setItem(homeItemSlot, item);

                if (player.hasPermission(this.permissionToDelete.getName())) {
                    final ItemStack delItem = createDeleteItem(pos);
                    final int delItemSlot = 16 + ((i - startIndex) * 9);
                    this.menu.setItem(delItemSlot, delItem);
                }
            }
        } catch (BasicHomesException e) {
            this.player.closeInventory();
            ChatUtils.sendPlayerError(this.player, "An error has occurred while loading the menu. Please try again. If this error occurs again, please contact an administrator and mention this error:");
            ChatUtils.sendPlayerError(this.player, e.getMessage());
        }
    }

    private void addPaginationButtons(final int totalPages) throws BasicHomesException {
        final ItemStack prevPageItem = createNavigationItem(Constants.PREVIOUS_PAGE_ITEM_NAME);
        final ItemStack nextPageItem = createNavigationItem(Constants.NEXT_PAGE_ITEM_NAME);

        if (this.page > 1) {
            this.menu.setItem(48, prevPageItem);
        }
        if (this.page < totalPages) {
            this.menu.setItem(50, nextPageItem);
        }

        final ItemStack pageIndicatorItem = createPageIndicatorItem(totalPages);
        this.menu.setItem(49, pageIndicatorItem);
    }

    private ItemStack createNavigationItem(final String displayName) throws BasicHomesException {
        final ItemStack item = new ItemStack(Constants.NAVIGATION_ITEM);
        final ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) throw new BasicHomesException("Could not generate the navigation item: item meta is null.");

        itemMeta.setDisplayName(displayName);
        item.setItemMeta(itemMeta);
        return item;
    }

    private ItemStack createPageIndicatorItem(final int totalPages) throws BasicHomesException {
        final ItemStack item = new ItemStack(Constants.PAGE_INDICATOR_ITEM);
        final ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) throw new BasicHomesException("Could not generate the page indicator item: item meta is null.");

        itemMeta.setDisplayName(String.format("Page %d/%d", this.page, totalPages));
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack createItem(@NonNull final SavedPosition pos) throws BasicHomesException {
        final ItemStack item = new ItemStack(pos.getBlock());
        final List<String> lore = new ArrayList<>();
        final ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) throw new BasicHomesException("Could not generate the item: item meta is null.");

        itemMeta.setDisplayName(pos.getName());
        lore.add("Click to teleport!");
        itemMeta.setLore(lore);
        itemMeta.getPersistentDataContainer().set(Constants.NAMESPACED_KEY_PAGINATED_MENU_ITEMS, PersistentDataType.STRING, pos.getId().toString());
        item.setItemMeta(itemMeta);

        return item;
    }

    public ItemStack createDeleteItem(@NonNull final SavedPosition pos) throws BasicHomesException {
        final ItemStack item = new ItemStack(Constants.DELETE_ITEM);
        final ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) throw new BasicHomesException("Could not generate the delete item: item meta is null.");

        itemMeta.setDisplayName(String.format(Constants.DELETE_ITEM_NAME, pos.getType().getDisplayName()));
        itemMeta.getPersistentDataContainer().set(Constants.NAMESPACED_KEY_PAGINATED_MENU_ITEMS, PersistentDataType.STRING, pos.getId().toString());
        item.setItemMeta(itemMeta);

        return item;
    }
}
