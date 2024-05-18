package fr.gamecreep.basichomes.utils;

import fr.gamecreep.basichomes.Constants;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class LoggerUtils {
    private final String prefix;
    private final ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();

    public LoggerUtils(final String prefix) {
        this.prefix = prefix;
    }
    public void logInfo(final String message) {
       this.consoleCommandSender.sendMessage(String.format("%s%s %s", Constants.INFO_COLOR, prefix, message));
    }

    public void logWarning(final String message) {
        this.consoleCommandSender.sendMessage(String.format("%s%s %s", Constants.WARNING_COLOR, prefix, message));
    }

}
