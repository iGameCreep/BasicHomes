package fr.gamecreep.basichomes.utils;

import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.enums.Permission;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChatUtils {

    public static final String PREFIX = String.format("%s[%sBH%s] ", ChatColor.GRAY, Constants.PLUGIN_COLOR, ChatColor.GRAY);

    public static void sendPlayerError(@NonNull final CommandSender target, final String message) {
        target.sendMessage(PREFIX + Constants.WARNING_COLOR + message);
    }

    public static void sendPlayerInfo(@NonNull final CommandSender target, final String message) {
        target.sendMessage(PREFIX + Constants.SUCCESS_COLOR + message);
    }

    public static void sendNoPermission(CommandSender target, Permission permission) {
        sendPlayerError(target, String.format(
                "You are not allowed to use this command. Permission required: %s",
                permission.getName()
        ));
    }
}