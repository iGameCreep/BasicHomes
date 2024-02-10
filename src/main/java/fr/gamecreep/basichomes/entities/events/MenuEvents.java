package fr.gamecreep.basichomes.entities.events;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.homes.PlayerHome;
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

        if (event.getView().getTitle().equals(Constants.HOMES_MENU_NAME)) {
            ItemStack pageIndicatorItem = Objects.requireNonNull(inv).getItem(49);
            ItemMeta meta = Objects.requireNonNull(pageIndicatorItem).getItemMeta();
            List<String> lore = Objects.requireNonNull(meta).getLore();
            int page = Integer.parseInt(Objects.requireNonNull(lore).get(0));

            playerSender.closeInventory();
            if (item == null || item.getType().equals(Constants.PAGE_INDICATOR_ITEM)) {
                this.plugin.getHomesUtils().openHomeInventory(playerSender, page);
                return;
            }

            String displayName = Objects.requireNonNull(Objects.requireNonNull(item).getItemMeta()).getDisplayName();

            if (displayName.equals(Constants.PREVIOUS_PAGE_ITEM_NAME)) {
                this.plugin.getHomesUtils().openHomeInventory(playerSender, page - 1);
            } else if (displayName.equals(Constants.NEXT_PAGE_ITEM_NAME)) {
                this.plugin.getHomesUtils().openHomeInventory(playerSender, page + 1);
            } else if (displayName.equals(Constants.DELETE_HOME_ITEM_NAME)) {
                String homeName = Objects.requireNonNull(item.getItemMeta().getLore()).get(0);
                PlayerHome home = this.plugin.getHomeByName(playerSender, homeName);
                if (home == null) {
                    this.plugin.getChatUtils().sendPlayerError(playerSender, "Could not delete the home.");
                    return;
                }
                this.plugin.removeHome(home);
                this.plugin.getChatUtils().sendPlayerInfo(playerSender, String.format("Home %s%s%s has been removed !", Constants.SPECIAL_COLOR, home.getHomeName(), Constants.SUCCESS_COLOR));
            } else {
                // Item clicked is a home
                String name = item.getItemMeta().getDisplayName();
                PlayerHome home = this.plugin.getHomeByName(playerSender, name);
                if (home == null) {
                    this.plugin.getChatUtils().sendPlayerError(playerSender, "Could not retrieve the home.");
                    return;
                }

                Location location = home.getLocation();
                location.setPitch(playerSender.getLocation().getPitch());
                location.setYaw(playerSender.getLocation().getYaw());
                playerSender.teleport(location);

                this.plugin.getChatUtils().sendPlayerInfo(playerSender, String.format("Teleporting you to %s%s%s...", Constants.SPECIAL_COLOR, home.getHomeName(), Constants.SUCCESS_COLOR));
            }
        }
    }

}
