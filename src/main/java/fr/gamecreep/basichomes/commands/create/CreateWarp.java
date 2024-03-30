package fr.gamecreep.basichomes.commands.create;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;

public class CreateWarp extends CreateCommand {

    public CreateWarp(BasicHomes plugin) {
        super(plugin, PositionType.WARP, Permission.CREATE_WARP);
    }
}
