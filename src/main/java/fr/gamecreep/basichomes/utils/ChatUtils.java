package fr.gamecreep.basichomes.utils;

import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.enums.Permission;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatUtils {
    private static final String PREFIX = String.format("%s[%sBH%s] ", ChatColor.GRAY, Constants.PLUGIN_COLOR, ChatColor.GRAY);

    public static void sendPlayerError(@NonNull final Player player, final String message) {
        player.sendMessage(PREFIX + Constants.WARNING_COLOR + message);
    }

    public static void sendPlayerInfo(@NonNull final Player player, final String message) {
        player.sendMessage(PREFIX + Constants.SUCCESS_COLOR + message);
    }

    public static void sendNoPermission(Player player, Permission permission) {
        sendPlayerError(player, String.format(
                "You are not allowed to use this command. Permission required: %s",
                permission.getName()
        ));
    }
}