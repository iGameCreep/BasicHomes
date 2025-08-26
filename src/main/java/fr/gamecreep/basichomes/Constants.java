package fr.gamecreep.basichomes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String PLUGIN_VERSION = "1.9.7";
    public static final int BSTATS_PLUGIN_ID = 25429;

    public static final ChatColor INFO_COLOR = ChatColor.DARK_AQUA;
    public static final ChatColor SUCCESS_COLOR = ChatColor.GREEN;
    public static final ChatColor WARNING_COLOR = ChatColor.RED;
    public static final ChatColor SPECIAL_COLOR = ChatColor.GOLD;

    public static final String PLUGIN_COLOR;
    static {
        String colorTemp;
        try {
            colorTemp = net.md_5.bungee.api.ChatColor.of("#2596be").toString();
        } catch (Throwable t) {
            colorTemp = ChatColor.DARK_AQUA.toString();
        }
        PLUGIN_COLOR = colorTemp;
    }


    public static final String MY_HOMES_MENU_NAME = SPECIAL_COLOR + "My Homes";
    public static final String HOMES_OF_START_MENU_NAME = SPECIAL_COLOR + "Homes of" + INFO_COLOR + " ";
    public static final String WARPS_MENU_NAME = SPECIAL_COLOR + "Warps";

    public static final String PREVIOUS_PAGE_ITEM_NAME = SPECIAL_COLOR + "<-- Previous Page";
    public static final String NEXT_PAGE_ITEM_NAME = SPECIAL_COLOR + "Next Page -->";
    public static final String DELETE_ITEM_NAME = WARNING_COLOR + "Delete this %s";

    public static final Material PAGE_INDICATOR_ITEM = Material.ENDER_EYE;
    public static final Material NAVIGATION_ITEM = Material.PAPER;
    public static final Material DELETE_ITEM = Material.BARRIER;
    public static final Material HOME_OVERWORLD_ITEM = Material.GRASS_BLOCK;
    public static final Material HOME_NETHER_ITEM = Material.CRIMSON_NYLIUM;
    public static final Material HOME_END_ITEM = Material.END_PORTAL_FRAME;
    public static final Material DEFAULT_HOME_ITEM = Material.ENDER_PEARL;

    public static final String NAMESPACED_KEY_PAGINATED_MENU_ITEMS = "items-id-paginated-menu";
}
