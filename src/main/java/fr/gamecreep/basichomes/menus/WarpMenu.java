package fr.gamecreep.basichomes.menus;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;

public class WarpMenu extends DefaultMenu {
    public WarpMenu(BasicHomes plugin) {
        super(plugin, PositionType.WARP, Permission.DELETE_WARP);
    }
}
