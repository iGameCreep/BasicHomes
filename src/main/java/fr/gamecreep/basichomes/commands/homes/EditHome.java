package fr.gamecreep.basichomes.commands.homes;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.commands.utils.EditCommand;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import lombok.NonNull;

public class EditHome extends EditCommand {
    public EditHome(@NonNull final BasicHomes plugin) {
        super(plugin, PositionType.HOME, Permission.USE_HOME);
    }
}
