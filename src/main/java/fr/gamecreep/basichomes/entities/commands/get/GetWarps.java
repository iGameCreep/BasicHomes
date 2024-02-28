package fr.gamecreep.basichomes.entities.commands.get;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.menus.WarpMenu;

public class GetWarps extends GetCommand {

    public GetWarps(BasicHomes plugin) {
        super(plugin, Permission.USE_WARP, new WarpMenu(plugin));
    }
}
