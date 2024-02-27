package fr.gamecreep.basichomes.entities.commands.create;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.PositionType;

public class CreateWarp extends CreateCommand {

    public CreateWarp(BasicHomes plugin) {
        super(plugin, PositionType.WARP);
    }
}
