package fr.gamecreep.basichomes.commands.homes;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.commands.utils.DeleteCommand;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;

public class DeleteHome extends DeleteCommand {

    public DeleteHome(BasicHomes plugin) {
        super(plugin, PositionType.HOME, Permission.USE_HOME);
    }
}
