package fr.gamecreep.basichomes.commands;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisabledCommand {
    private final BasicHomes plugin;
    private final PositionType type;

    public DisabledCommand(BasicHomes plugin, PositionType type) {
        this.plugin = plugin;
        this.type = type;
    }

    public boolean onCommand(@NonNull CommandSender commandSender) {
        if (commandSender instanceof Player playerSender) {
            String message = String.format("The %ss are disabled on this server !", this.type.getDisplayName());

            this.plugin.getChatUtils().sendPlayerError(playerSender, message);
            return true;
        }

        return false;
    }
}
