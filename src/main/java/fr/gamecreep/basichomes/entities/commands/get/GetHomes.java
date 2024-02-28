package fr.gamecreep.basichomes.entities.commands.get;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.menus.HomeMenu;

public class GetHomes extends GetCommand {

    public GetHomes(BasicHomes plugin) {
        super(plugin, Permission.USE_HOME, new HomeMenu(plugin));
    }
}
