package fr.gamecreep.basichomes.menus;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.MenuType;
import fr.gamecreep.basichomes.entities.enums.Permission;

public class WarpMenu extends DefaultMenu {
    public WarpMenu(BasicHomes plugin) {
        super(plugin, MenuType.WARP, Permission.DELETE_WARP);
    }
}
