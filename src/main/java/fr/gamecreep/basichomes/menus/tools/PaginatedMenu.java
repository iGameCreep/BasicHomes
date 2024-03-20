package fr.gamecreep.basichomes.menus.tools;

import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.classes.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.Permission;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

@Getter
public abstract class PaginatedMenu {

    private final Inventory menu;
    private final Player player;
    private int page = 1;
    private final Permission permissionToDelete;
    private List<SavedPosition> data = Collections.emptyList();

    protected PaginatedMenu(@NonNull final Player player,
                            @NonNull final Player target,
                            final String menuName,
                            final Permission permissionToDelete) {
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

    protected final void populateMenu(List<SavedPosition> data) {
        this.data = data;
        int itemsPerPage = 4;
        int totalItems = data.size();
        int totalPages = totalItems / itemsPerPage;
        if (totalItems % itemsPerPage != 0) totalPages++;

        addPaginationButtons(totalPages);

        int startIndex = (this.page - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        for (int i = startIndex; i < endIndex; i++) {
            SavedPosition pos = data.get(i);

            ItemStack item = createItem(pos);
            int homeItemSlot = 10 + ((i - startIndex) * 9);
            this.menu.setItem(homeItemSlot, item);

            if (player.hasPermission(this.permissionToDelete.getName())) {
                ItemStack delItem = createDeleteItem(pos);
                int delItemSlot = 16 + ((i - startIndex) * 9);
                this.menu.setItem(delItemSlot, delItem);
            }
        }
    }

    private void addPaginationButtons(final int totalPages) {
        ItemStack prevPageItem = createNavigationItem(Constants.PREVIOUS_PAGE_ITEM_NAME);
        ItemStack nextPageItem = createNavigationItem(Constants.NEXT_PAGE_ITEM_NAME);

        if (this.page > 1) {
            this.menu.setItem(48, prevPageItem);
        }
        if (this.page < totalPages) {
            this.menu.setItem(50, nextPageItem);
        }

        ItemStack pageIndicatorItem = createPageIndicatorItem(totalPages);
        this.menu.setItem(49, pageIndicatorItem);
    }

    private ItemStack createNavigationItem(String displayName) {
        ItemStack item = new ItemStack(Constants.NAVIGATION_ITEM);
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(displayName);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createPageIndicatorItem(int totalPages) {
        ItemStack item = new ItemStack(Constants.PAGE_INDICATOR_ITEM);
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(String.format("Page %d/%d", this.page, totalPages));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createItem(@NonNull final SavedPosition pos) {
        Material material = switch (pos.getWorld()) {
            case "world" -> Constants.HOME_OVERWORLD_ITEM;
            case "world_nether" -> Constants.HOME_NETHER_ITEM;
            case "world_the_end" -> Constants.HOME_END_ITEM;
            default -> Constants.DEFAULT_HOME_ITEM;
        };

        ItemStack item = new ItemStack(material);
        List<String> lore = new ArrayList<>();

        ItemMeta itemMeta = item.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(pos.getName());
        lore.add("Click to teleport!");
        itemMeta.setLore(lore);
        itemMeta.getPersistentDataContainer().set(Constants.NAMESPACED_KEY_PAGINATED_MENU_ITEMS, PersistentDataType.STRING, pos.getId().toString());
        item.setItemMeta(itemMeta);

        return item;
    }

    public ItemStack createDeleteItem(@NonNull final SavedPosition pos) {
        ItemStack delItem = new ItemStack(Constants.DELETE_ITEM);

        ItemMeta delItemMeta = delItem.getItemMeta();
        Objects.requireNonNull(delItemMeta).setDisplayName(String.format(Constants.DELETE_ITEM_NAME, pos.getType().getDisplayName()));
        delItemMeta.getPersistentDataContainer().set(Constants.NAMESPACED_KEY_PAGINATED_MENU_ITEMS, PersistentDataType.STRING, pos.getId().toString());
        delItem.setItemMeta(delItemMeta);

        return delItem;
    }
}
