package fr.gamecreep.basichomes.entities.events;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.homes.PlayerHome;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
        assert inv != null;

        if (event.getView().getTitle().equals("§bMy Homes")) {
            int page = Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(inv.getItem(49)).getItemMeta()).getDisplayName().replaceAll("[^0-9/]+", "").split("/")[0]);
            player.closeInventory();
            if (item == null) {
                plugin.getHomesUtils().openHomeInventory(player, page);
                return;
            }
            switch (Objects.requireNonNull(Objects.requireNonNull(item).getItemMeta()).getDisplayName()) {
                case "§6<-- Previous Page":
                    plugin.getHomesUtils().openHomeInventory(player, page - 1);
                    break;
                case "§6Next Page -->":
                    plugin.getHomesUtils().openHomeInventory(player, page + 1);
                    break;
                case "§cDelete this home":
                    String homeName = Objects.requireNonNull(item.getItemMeta().getLore()).get(0);
                    PlayerHome home = plugin.getHomeByName(player, homeName);
                    if (home == null) {
                        plugin.getChatUtils().sendPlayerError(player, "Could not delete the home.");
                        return;
                    }
                    plugin.removeHome(home);
                    plugin.getChatUtils().sendPlayerInfo(player, String.format("Home §e%s§b has been removed !", home.getHomeName()));
                    break;
                default:
                    if (item.getType().equals(Material.ENDER_EYE)) {
                        // Item clicked is the page indicator
                        plugin.getHomesUtils().openHomeInventory(player, page);
                        break;
                    }
                    // Item clicked is a home
			        String name = item.getItemMeta().getDisplayName();
                    PlayerHome playerHome = plugin.getHomeByName(player, name);
                    if (playerHome == null) {
                        plugin.getChatUtils().sendPlayerError(player, "Could not get the home.");
                        return;
                    }
                    player.teleport(playerHome.getLocation());
                    plugin.getChatUtils().sendPlayerInfo(player, "Teleporting you to §e" + playerHome.getHomeName() + "§b...");
			        break;
            }
        }
    }

}
