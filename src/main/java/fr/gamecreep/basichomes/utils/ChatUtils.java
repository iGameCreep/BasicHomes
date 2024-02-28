package fr.gamecreep.basichomes.utils;

import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.enums.Permission;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class ChatUtils {
    public void sendPlayerError(@NonNull final Player player, final String message) {
        player.sendMessage(Constants.WARNING_COLOR + message);
    }

    public void sendPlayerInfo(@NonNull final Player player, final String message) {
        player.sendMessage(Constants.SUCCESS_COLOR + message);
    }

    public void sendNoPermission(Player player, Permission permission) {
        this.sendPlayerError(player, String.format(
                "You are not allowed to use this command. Permission required: %s",
                permission.getName()
        ));
    }
}