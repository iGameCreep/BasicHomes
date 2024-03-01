package fr.gamecreep.basichomes.commands.disabled;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.PositionType;

public class WarpsDisabled extends DisabledCommand {
    public WarpsDisabled(BasicHomes plugin) {
        super(plugin, PositionType.WARP);
    }
}
