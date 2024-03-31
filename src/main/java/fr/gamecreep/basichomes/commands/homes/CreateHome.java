package fr.gamecreep.basichomes.commands.homes;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.commands.utils.CreateCommand;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;

public class CreateHome extends CreateCommand {

    public CreateHome(BasicHomes plugin) {
        super(plugin, PositionType.HOME, Permission.USE_HOME);
    }
}
