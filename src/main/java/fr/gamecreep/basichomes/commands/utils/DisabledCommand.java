package fr.gamecreep.basichomes.commands.utils;

import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.utils.ChatUtils;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisabledCommand {
    private final PositionType type;

    public DisabledCommand(final PositionType type) {
        this.type = type;
    }

    public boolean onCommand(@NonNull final CommandSender commandSender) {
        if (commandSender instanceof final Player playerSender) {
            final String message = String.format("The %ss are disabled on this server !", this.type.getDisplayName());

            ChatUtils.sendPlayerError(playerSender, message);
            return true;
        }

        return false;
    }
}
