package fr.gamecreep.basichomes.entities.commands.delete;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.PositionType;

public class DeleteHome extends DeleteCommand {

    public DeleteHome(BasicHomes plugin) {
        super(plugin, PositionType.HOME);
    }
}
