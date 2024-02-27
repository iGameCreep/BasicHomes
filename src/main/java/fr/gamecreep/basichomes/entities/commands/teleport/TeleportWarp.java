package fr.gamecreep.basichomes.entities.commands.teleport;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.PositionType;

public class TeleportWarp extends TeleportCommand {

    public TeleportWarp(BasicHomes plugin) {
        super(plugin, PositionType.WARP);
    }
}
