package fr.gamecreep.basichomes.commands.warps;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.commands.utils.DeleteCommand;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;

public class DeleteWarp extends DeleteCommand {
    public DeleteWarp(BasicHomes plugin) {
        super(plugin, PositionType.WARP, Permission.DELETE_WARP);
    }
}
