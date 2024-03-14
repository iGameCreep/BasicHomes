package fr.gamecreep.basichomes.events;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.classes.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.MenuType;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.files.DataHandler;
import fr.gamecreep.basichomes.menus.HomeMenu;
import fr.gamecreep.basichomes.menus.WarpMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class MenuEvents implements Listener {
    private final BasicHomes plugin;

    public MenuEvents(BasicHomes plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getClickedInventory();
        ItemStack item = event.getCurrentItem();
        String invTitle = event.getView().getTitle();
        if (inv == null) return;
        if (!(inv.getHolder() instanceof Player)) return;

        if (isMenu(invTitle)) {
            MenuType menuType = this.findTypeFromMenuName(invTitle);
            Player target = (Player) inv.getHolder();

            ItemStack pageIndicatorItem = Objects.requireNonNull(inv).getItem(49);
            ItemMeta meta = Objects.requireNonNull(pageIndicatorItem).getItemMeta();
            List<String> lore = Objects.requireNonNull(meta).getLore();
            int page = Integer.parseInt(Objects.requireNonNull(lore).get(0));

            if (item == null || item.getType().equals(Constants.PAGE_INDICATOR_ITEM)) {
                event.setCancelled(true);
                return;
            }

            this.handleClickByItem(item, menuType, page, player, target);
        }
    }

    private MenuType findTypeFromMenuName(String menuName) {
        if (menuName.equals(MenuType.WARP.getStartOfMenuName())) return MenuType.WARP;
        else if (menuName.equals(MenuType.HOME.getStartOfMenuName())) return MenuType.HOME;
        else return MenuType.HOME_OF;
    }

    private boolean isMenu(String name) {
        boolean isHomeMenu = name.equals(MenuType.HOME.getStartOfMenuName());
        boolean isWarpMenu = name.equals(MenuType.WARP.getStartOfMenuName());
        boolean isHomeOfMenu = name.startsWith(MenuType.HOME_OF.getStartOfMenuName());

        return (isHomeMenu || isWarpMenu || isHomeOfMenu);
    }

    private void handleClickByItem(ItemStack item, MenuType menuType, int page, Player player, Player target) {
        DataHandler handler = (menuType.getType() == PositionType.WARP) ? this.plugin.getWarpHandler() : this.plugin.getHomeHandler();
        String displayName = Objects.requireNonNull(Objects.requireNonNull(item).getItemMeta()).getDisplayName();

        player.closeInventory();
        if (displayName.equals(Constants.PREVIOUS_PAGE_ITEM_NAME)) {
            this.reopenInventory(player, target, menuType, page - 1);
        } else if (displayName.equals(Constants.NEXT_PAGE_ITEM_NAME)) {
            this.reopenInventory(player, target, menuType, page + 1);
        } else if (displayName.equals(menuType.getType().getDeleteText())) {
            String posName = Objects.requireNonNull(item.getItemMeta().getLore()).get(0);
            SavedPosition pos = handler.getByName(player, posName);
            if (pos == null) {
                this.plugin.getChatUtils().sendPlayerError(player, "Could not delete the home.");
                return;
            }
            handler.delete(pos);
            this.plugin.getChatUtils().sendPlayerInfo(player, String.format("Home %s%s%s has been removed !", Constants.SPECIAL_COLOR, pos.getName(), Constants.SUCCESS_COLOR));
        } else {
            // Item clicked is a home/warp
            String name = item.getItemMeta().getDisplayName();
            SavedPosition pos = handler.getByName(player, name);
            if (pos == null) {
                this.plugin.getChatUtils().sendPlayerError(player, String.format("Could not retrieve the %s.", menuType.getType().getDisplayName()));
                return;
            }

            PositionType posType = menuType == MenuType.HOME ? PositionType.HOME : PositionType.WARP;
            boolean success = this.plugin.getTeleportUtils().add(player, pos, posType);

            if (success) this.plugin.getChatUtils().sendPlayerInfo(player, String.format("Teleporting you to %s%s%s...", Constants.SPECIAL_COLOR, pos.getName(), Constants.SUCCESS_COLOR));
        }
    }

    private void reopenInventory(Player player, Player target, MenuType menuType, int page) {
        if (menuType == MenuType.HOME) {
            new HomeMenu(this.plugin).openInventory(player, page);
        } else if (menuType == MenuType.WARP) {
            new WarpMenu(this.plugin).openInventory(player, page);
        } else {
            new HomeMenu(this.plugin).openInventoryOf(player, target, page);
        }
    }
}
