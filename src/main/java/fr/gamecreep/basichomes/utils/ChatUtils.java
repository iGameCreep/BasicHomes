package fr.gamecreep.basichomes.utils;

import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils {
    public void sendPlayerError(@NonNull Player player, String message) {
        player.sendMessage(ChatColor.RED + message);
    }

    public void sendPlayerInfo(@NonNull Player player, String message) {
        player.sendMessage(ChatColor.AQUA + message);
    }
}
