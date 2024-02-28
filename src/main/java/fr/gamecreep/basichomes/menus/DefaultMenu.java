package fr.gamecreep.basichomes.menus;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.classes.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.files.DataHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class DefaultMenu implements Listener {
    private final BasicHomes plugin;
    private final PositionType type;
    private final Permission permission;
    private final DataHandler handler;

    protected DefaultMenu(BasicHomes plugin, PositionType type, Permission permission) {
        this.plugin = plugin;
        this.type = type;
        this.permission = permission;
        if (type == PositionType.HOME) this.handler = plugin.getHomeHandler();
        else this.handler = plugin.getWarpHandler();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player playerSender = (Player) event.getWhoClicked();
        Inventory inv = event.getClickedInventory();
        ItemStack item = event.getCurrentItem();

        if (event.getView().getTitle().equals(this.type.getMenuName())) {
            ItemStack pageIndicatorItem = Objects.requireNonNull(inv).getItem(49);
            ItemMeta meta = Objects.requireNonNull(pageIndicatorItem).getItemMeta();
            List<String> lore = Objects.requireNonNull(meta).getLore();
            int page = Integer.parseInt(Objects.requireNonNull(lore).get(0));

            if (item == null || item.getType().equals(Constants.PAGE_INDICATOR_ITEM)) {
                event.setCancelled(true);
                return;
            }

            String displayName = Objects.requireNonNull(Objects.requireNonNull(item).getItemMeta()).getDisplayName();

            playerSender.closeInventory();
            if (displayName.equals(Constants.PREVIOUS_PAGE_ITEM_NAME)) {
                this.openInventory(playerSender, page - 1);
            } else if (displayName.equals(Constants.NEXT_PAGE_ITEM_NAME)) {
                this.openInventory(playerSender, page + 1);
            } else if (displayName.equals(this.type.getDeleteText())) {
                String posName = Objects.requireNonNull(item.getItemMeta().getLore()).get(0);
                SavedPosition pos = this.handler.getByName(playerSender, posName);
                if (pos == null) {
                    this.plugin.getChatUtils().sendPlayerError(playerSender, "Could not delete the home.");
                    return;
                }
                this.handler.delete(pos);
                this.plugin.getChatUtils().sendPlayerInfo(playerSender, String.format("Home %s%s%s has been removed !", Constants.SPECIAL_COLOR, pos.getName(), Constants.SUCCESS_COLOR));
            } else {
                // Item clicked is a home/warp
                String name = item.getItemMeta().getDisplayName();
                SavedPosition pos = this.handler.getByName(playerSender, name);
                if (pos == null) {
                    this.plugin.getChatUtils().sendPlayerError(playerSender, String.format("Could not retrieve the %s.", this.type.getDisplayName()));
                    return;
                }

                Location location = pos.getLocation();
                location.setPitch(playerSender.getLocation().getPitch());
                location.setYaw(playerSender.getLocation().getYaw());
                playerSender.teleport(location);

                this.plugin.getChatUtils().sendPlayerInfo(playerSender, String.format("Teleporting you to %s%s%s...", Constants.SPECIAL_COLOR, pos.getName(), Constants.SUCCESS_COLOR));
            }
        }
    }

    public void openInventory(Player player, int currentPage) {
        final int dataPerPage = 4;
        List<SavedPosition> list;
        if (this.type == PositionType.HOME) list = this.handler.getAllByPlayer(player);
        else list = this.handler.getAll();

        int totalPages = (int) Math.ceil((double) list.size() / dataPerPage);

        Inventory inventory = Bukkit.createInventory(null, 54, this.type.getMenuName());

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
        Objects.requireNonNull(delItemMeta).setDisplayName(this.type.getDeleteText());
        lore.add(pos.getName());
        delItemMeta.setLore(lore);
        delItem.setItemMeta(delItemMeta);

        return delItem;
    }
}
