package fr.gamecreep.basichomes.events;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.classes.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.MenuType;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.files.DataHandler;
import fr.gamecreep.basichomes.menus.HomeMenu;
import fr.gamecreep.basichomes.menus.WarpMenu;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
        Player playerSender = (Player) event.getWhoClicked();
        Inventory inv = event.getClickedInventory();
        ItemStack item = event.getCurrentItem();
        String invTitle = event.getView().getTitle();

        if (isMenu(invTitle)) {
            MenuType menuType = this.findTypeFromMenuName(invTitle);
            DataHandler handler = (menuType.getType() == PositionType.WARP) ? this.plugin.getWarpHandler() : this.plugin.getHomeHandler();

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
                this.reopenInventory(playerSender, menuType, invTitle, page - 1);
            } else if (displayName.equals(Constants.NEXT_PAGE_ITEM_NAME)) {
                this.reopenInventory(playerSender, menuType, invTitle, page + 1);
            } else if (displayName.equals(menuType.getType().getDeleteText())) {
                String posName = Objects.requireNonNull(item.getItemMeta().getLore()).get(0);
                SavedPosition pos = handler.getByName(playerSender, posName);
                if (pos == null) {
                    this.plugin.getChatUtils().sendPlayerError(playerSender, "Could not delete the home.");
                    return;
                }
                handler.delete(pos);
                this.plugin.getChatUtils().sendPlayerInfo(playerSender, String.format("Home %s%s%s has been removed !", Constants.SPECIAL_COLOR, pos.getName(), Constants.SUCCESS_COLOR));
            } else {
                // Item clicked is a home/warp
                String name = item.getItemMeta().getDisplayName();
                SavedPosition pos = handler.getByName(playerSender, name);
                if (pos == null) {
                    this.plugin.getChatUtils().sendPlayerError(playerSender, String.format("Could not retrieve the %s.", menuType.getType().getDisplayName()));
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

    private void reopenInventory(Player player, MenuType menuType, String invName, int page) {
        if (menuType == MenuType.HOME) {
            new HomeMenu(this.plugin).openInventory(player, page);
        } else if (menuType == MenuType.WARP) {
            new WarpMenu(this.plugin).openInventory(player, page);
        } else {
            String targetName = invName.split(String.valueOf(Constants.SPECIAL_COLOR))[0];
            Player target = Bukkit.getPlayerExact(targetName);
            new HomeMenu(this.plugin).openInventoryOf(player, target, page);
        }
    }
}
