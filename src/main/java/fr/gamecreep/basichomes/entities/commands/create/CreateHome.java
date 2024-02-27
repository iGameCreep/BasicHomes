package fr.gamecreep.basichomes.entities.commands.create;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.PositionType;

public class CreateHome extends CreateCommand {

    public CreateHome(BasicHomes plugin) {
        super(plugin, PositionType.HOME);
    }
}
