package fr.gamecreep.basichomes.commands.disabled;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class DisabledCommand implements CommandExecutor {
    private final BasicHomes plugin;
    private final PositionType type;

    protected DisabledCommand(BasicHomes plugin, PositionType type) {
        this.plugin = plugin;
        this.type = type;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender commandSender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (commandSender instanceof Player) {
            Player playerSender = (Player) commandSender;

            String message = String.format("The %ss are disabled on this server !", this.type.getDisplayName());
            this.plugin.getChatUtils().sendPlayerError(playerSender, message);
            return true;
        }

        return false;
    }
}
