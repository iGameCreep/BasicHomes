package fr.gamecreep.basichomes.commands.disabled;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.PositionType;

public class HomesDisabled extends DisabledCommand {
    public HomesDisabled(BasicHomes plugin) {
        super(plugin, PositionType.HOME);
    }
}
