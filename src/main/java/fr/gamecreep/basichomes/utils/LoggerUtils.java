package fr.gamecreep.basichomes.utils;

import fr.gamecreep.basichomes.Constants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoggerUtils {

    public static final String CONSOLE_PREFIX = String.format("%s[%sBH%s]", ChatColor.GRAY, ChatColor.BLUE, ChatColor.GRAY);
    private static final ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();

    public static void logInfo(final String message) {
        consoleCommandSender.sendMessage(String.format("%s%s %s", CONSOLE_PREFIX, Constants.INFO_COLOR, message));
    }

    public static void logWarning(final String message) {
        consoleCommandSender.sendMessage(String.format("%s%s %s", CONSOLE_PREFIX, Constants.WARNING_COLOR, message));
    }

}
