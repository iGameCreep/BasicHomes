package fr.gamecreep.basichomes.entities.commands.teleport;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.PositionType;

public class TeleportHome extends TeleportCommand {

    public TeleportHome(BasicHomes plugin) {
        super(plugin, PositionType.HOME);
    }
}
