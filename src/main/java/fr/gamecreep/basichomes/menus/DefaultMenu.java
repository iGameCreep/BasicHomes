package fr.gamecreep.basichomes.menus;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.classes.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.MenuType;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.files.DataHandler;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class DefaultMenu implements Listener {
    private final MenuType type;
    private final Permission permission;
    private final DataHandler handler;

    protected DefaultMenu(BasicHomes plugin, MenuType type, Permission permission) {
        this.type = type;
        this.permission = permission;
        if (type == MenuType.HOME) this.handler = plugin.getHomeHandler();
        else this.handler = plugin.getWarpHandler();
    }

    public void openInventory(@NonNull Player player, int currentPage) {
        final int dataPerPage = 4;
        List<SavedPosition> list;
        if (this.type.getType() == PositionType.HOME) list = this.handler.getAllByPlayer(player);
        else list = this.handler.getAll();

        int totalPages = (int) Math.ceil((double) list.size() / dataPerPage);

        Inventory inventory = Bukkit.createInventory(player, 54, this.type.getStartOfMenuName());

        int startIndex = (currentPage - 1) * dataPerPage;
        int endIndex = Math.min(startIndex + dataPerPage, list.size());

        for (int i = startIndex; i < endIndex; i++) {
            SavedPosition pos = list.get(i);

            ItemStack item = createItem(pos);
            int homeItemSlot = 10 + ((i - startIndex) * 9);
            inventory.setItem(homeItemSlot, item);

            if (player.hasPermission(this.permission.getName())) {
                ItemStack delItem = createDeleteItem(pos);
                int delItemSlot = 16 + ((i - startIndex) * 9);
                inventory.setItem(delItemSlot, delItem);
            }
        }

        addPaginationButtons(inventory, currentPage, totalPages);

        player.openInventory(inventory);
    }

    public void openInventoryOf(@NonNull Player player, @NonNull Player target, int currentPage) {
        final int dataPerPage = 4;
        List<SavedPosition> list;
        if (this.type.getType() == PositionType.HOME) list = this.handler.getAllByPlayer(target);
        else list = this.handler.getAll();

        int totalPages = (int) Math.ceil((double) list.size() / dataPerPage);

        String name = String.format("%s %s%s", Constants.HOMES_OF_START_MENU_NAME, Constants.SPECIAL_COLOR, target.getName());
        Inventory inventory = Bukkit.createInventory(target, 54, name);

        int startIndex = (currentPage - 1) * dataPerPage;
        int endIndex = Math.min(startIndex + dataPerPage, list.size());

        for (int i = startIndex; i < endIndex; i++) {
            SavedPosition pos = list.get(i);

            ItemStack item = createItem(pos);
            int homeItemSlot = 10 + ((i - startIndex) * 9);
            inventory.setItem(homeItemSlot, item);

            ItemStack delItem = createDeleteItem(pos);
            int delItemSlot = 16 + ((i - startIndex) * 9);
            inventory.setItem(delItemSlot, delItem);
        }

        addPaginationButtons(inventory, currentPage, totalPages);

        player.openInventory(inventory);
    }

    private void addPaginationButtons(Inventory inventory, int currentPage, int totalPages) {
        ItemStack prevPageItem = createNavigationItem(Constants.PREVIOUS_PAGE_ITEM_NAME);
        ItemStack nextPageItem = createNavigationItem(Constants.NEXT_PAGE_ITEM_NAME);

        if (currentPage > 1) {
            inventory.setItem(48, prevPageItem);
        }
        if (currentPage < totalPages) {
            inventory.setItem(50, nextPageItem);
        }

        ItemStack pageIndicatorItem = createPageIndicatorItem(currentPage, totalPages);
        inventory.setItem(49, pageIndicatorItem);
    }

    private ItemStack createNavigationItem(String displayName) {
        ItemStack item = new ItemStack(Constants.NAVIGATION_ITEM);
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(displayName);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createPageIndicatorItem(int currentPage, int totalPages) {
        ItemStack item = new ItemStack(Constants.PAGE_INDICATOR_ITEM);
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setLore(List.of(String.valueOf(currentPage)));
        Objects.requireNonNull(meta).setDisplayName("Page " + currentPage + "/" + totalPages);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createItem(SavedPosition pos) {
        Material material;

        switch (pos.getWorld()) {
            case "world":
                material = Constants.HOME_OVERWORLD_ITEM;
                break;
            case "world_nether":
                material = Constants.HOME_NETHER_ITEM;
                break;
            case "world_the_end":
                material = Constants.HOME_END_ITEM;
                break;
            default:
                material = Constants.DEFAULT_HOME_ITEM;
                break;
        }

        ItemStack item = new ItemStack(material);
        List<String> lore = new ArrayList<>();

        ItemMeta itemMeta = item.getItemMeta();
        Objects.requireNonNull(itemMeta).setDisplayName(pos.getName());
        lore.add("Click to teleport!");
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

    private ItemStack createDeleteItem(SavedPosition pos) {
        ItemStack delItem = new ItemStack(Constants.DELETE_ITEM);
        List<String> lore = new ArrayList<>();

        ItemMeta delItemMeta = delItem.getItemMeta();
        Objects.requireNonNull(delItemMeta).setDisplayName(this.type.getType().getDeleteText());
        lore.add(pos.getName());
        delItemMeta.setLore(lore);
        delItem.setItemMeta(delItemMeta);

        return delItem;
    }
}
