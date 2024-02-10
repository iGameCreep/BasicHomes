package fr.gamecreep.basichomes.utils;

import org.bukkit.ChatColor;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class ChatUtils
{
    public void sendPlayerError(@NonNull final Player player, final String message) {
        player.sendMessage(ChatColor.RED + message);
    }

    public void sendPlayerInfo(@NonNull final Player player, final String message) {
        player.sendMessage(ChatColor.AQUA + message);
    }
}