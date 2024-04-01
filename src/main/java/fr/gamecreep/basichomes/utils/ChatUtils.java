package fr.gamecreep.basichomes.utils;

import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.enums.Permission;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils {
    private static final String PREFIX = String.format("%s[%sBH%s] ", ChatColor.GRAY, Constants.PLUGIN_COLOR, ChatColor.GRAY);

    public void sendPlayerError(@NonNull final Player player, final String message) {
        player.sendMessage(PREFIX + Constants.WARNING_COLOR + message);
    }

    public void sendPlayerInfo(@NonNull final Player player, final String message) {
        player.sendMessage(PREFIX + Constants.SUCCESS_COLOR + message);
    }

    public void sendNoPermission(Player player, Permission permission) {
        this.sendPlayerError(player, String.format(
                "You are not allowed to use this command. Permission required: %s",
                permission.getName()
        ));
    }
}