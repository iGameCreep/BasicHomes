package fr.gamecreep.basichomes.utils;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.homes.PlayerHome;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomesUtils {
    private final BasicHomes plugin;

    public HomesUtils (BasicHomes plugin) {
        this.plugin = plugin;
    }

    public void openHomeInventory(Player player, int currentPage) {
        final int homesPerPage = 4;
        List<PlayerHome> homes = plugin.getAllPlayerHomes(player);

        int totalPages = (int) Math.ceil((double) homes.size() / homesPerPage);

        Inventory inventory = Bukkit.createInventory(null, 54, "§bMy Homes");

        int startIndex = (currentPage - 1) * homesPerPage;
        int endIndex = Math.min(startIndex + homesPerPage, homes.size());

        for (int i = startIndex; i < endIndex; i++) {
            PlayerHome home = homes.get(i);

            ItemStack homeItem = createHomeItem(home);
            int homeItemSlot = 10 + ((i - startIndex) * 9);
            inventory.setItem(homeItemSlot, homeItem);

            ItemStack delItem = createDelHomeItem(home);
            int delItemSlot = 16 + ((i - startIndex) * 9);
            inventory.setItem(delItemSlot, delItem);
        }

        addPaginationButtons(inventory, currentPage, totalPages);

        player.openInventory(inventory);
    }

    private void addPaginationButtons(Inventory inventory, int currentPage, int totalPages) {
        ItemStack prevPageItem = createNavigationItem("§6<-- Previous Page");
        ItemStack nextPageItem = createNavigationItem("§6Next Page -->");

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
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName(displayName);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createPageIndicatorItem(int currentPage, int totalPages) {
        ItemStack item = new ItemStack(Material.ENDER_EYE);
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta).setDisplayName("Page " + currentPage + "/" + totalPages);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createHomeItem(PlayerHome home) {
        Material material;

        switch (home.getWorld()) {
            case "world":
                material = Material.GRASS_BLOCK;
                break;
            case "world_nether":
                material = Material.CRIMSON_NYLIUM;
                break;
            case "world_the_end":
                material = Material.END_PORTAL_FRAME;
                break;
            default:
                material = Material.ENDER_PEARL;
                break;
        }

        ItemStack homeItem = new ItemStack(material);
        List<String> lore = new ArrayList<>();

        ItemMeta homeItemMeta = homeItem.getItemMeta();
        Objects.requireNonNull(homeItemMeta).setDisplayName(home.getHomeName());
        lore.add("Click to teleport!");
        homeItemMeta.setLore(lore);
        homeItem.setItemMeta(homeItemMeta);

        return homeItem;
    }
    private ItemStack createDelHomeItem(PlayerHome home) {
        ItemStack delItem = new ItemStack(Material.BARRIER);
        List<String> lore = new ArrayList<>();

        ItemMeta delItemMeta = delItem.getItemMeta();
        Objects.requireNonNull(delItemMeta).setDisplayName("§cDelete this home");
        lore.add(home.getHomeName());
        delItemMeta.setLore(lore);
        delItem.setItemMeta(delItemMeta);

        return delItem;
    }
}
