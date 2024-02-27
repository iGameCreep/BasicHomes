package fr.gamecreep.basichomes;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class Constants {
    private Constants() {}

    public static final ChatColor INFO_COLOR = ChatColor.DARK_AQUA;
    public static final ChatColor SUCCESS_COLOR = ChatColor.GREEN;
    public static final ChatColor WARNING_COLOR = ChatColor.RED;
    public static final ChatColor SPECIAL_COLOR = ChatColor.GOLD;

    public static final String PREVIOUS_PAGE_ITEM_NAME = SPECIAL_COLOR + "<-- Previous Page";
    public static final String NEXT_PAGE_ITEM_NAME = SPECIAL_COLOR + "Next Page -->";

    public static final Material PAGE_INDICATOR_ITEM = Material.ENDER_EYE;
    public static final Material NAVIGATION_ITEM = Material.PAPER;
    public static final Material DELETE_ITEM = Material.BARRIER;
    public static final Material HOME_OVERWORLD_ITEM = Material.GRASS_BLOCK;
    public static final Material HOME_NETHER_ITEM = Material.CRIMSON_NYLIUM;
    public static final Material HOME_END_ITEM = Material.END_PORTAL_FRAME;
    public static final Material DEFAULT_HOME_ITEM = Material.ENDER_PEARL;

}
