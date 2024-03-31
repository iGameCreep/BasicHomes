package fr.gamecreep.basichomes.commands.warps;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.commands.utils.TeleportCommand;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;

public class TeleportWarp extends TeleportCommand {

    public TeleportWarp(BasicHomes plugin) {
        super(plugin, PositionType.WARP, Permission.USE_WARP);
    }
}
