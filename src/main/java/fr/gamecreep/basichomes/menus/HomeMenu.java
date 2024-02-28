package fr.gamecreep.basichomes.menus;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.MenuType;
import fr.gamecreep.basichomes.entities.enums.Permission;

public class HomeMenu extends DefaultMenu {
    public HomeMenu(BasicHomes plugin) {
        super(plugin, MenuType.HOME, Permission.USE_HOME);
    }
}
