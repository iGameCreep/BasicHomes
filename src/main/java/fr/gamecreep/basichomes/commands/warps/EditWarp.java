package fr.gamecreep.basichomes.commands.warps;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.commands.utils.EditCommand;
import fr.gamecreep.basichomes.entities.enums.Permission;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import lombok.NonNull;

public class EditWarp extends EditCommand {
    public EditWarp(@NonNull final BasicHomes plugin) {
        super(plugin, PositionType.WARP, Permission.UPDATE_WARP);
    }
}
