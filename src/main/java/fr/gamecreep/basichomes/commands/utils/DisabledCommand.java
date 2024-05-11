package fr.gamecreep.basichomes.commands.utils;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisabledCommand {
    private final BasicHomes plugin;
    private final PositionType type;

    public DisabledCommand(final BasicHomes plugin, final PositionType type) {
        this.plugin = plugin;
        this.type = type;
    }

    public boolean onCommand(@NonNull final CommandSender commandSender) {
        if (commandSender instanceof final Player playerSender) {
            final String message = String.format("The %ss are disabled on this server !", this.type.getDisplayName());

            this.plugin.getChatUtils().sendPlayerError(playerSender, message);
            return true;
        }

        return false;
    }
}
