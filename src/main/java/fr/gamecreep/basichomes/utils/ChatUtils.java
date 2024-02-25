package fr.gamecreep.basichomes.utils;

import fr.gamecreep.basichomes.Constants;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class ChatUtils {
    public void sendPlayerError(@NonNull final Player player, final String message) {
        player.sendMessage(Constants.WARNING_COLOR + message);
    }

    public void sendPlayerInfo(@NonNull final Player player, final String message) {
        player.sendMessage(Constants.SUCCESS_COLOR + message);
    }
}