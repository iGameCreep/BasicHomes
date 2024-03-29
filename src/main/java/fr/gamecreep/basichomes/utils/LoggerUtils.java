package fr.gamecreep.basichomes.utils;

import fr.gamecreep.basichomes.Constants;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class LoggerUtils {
    private final String PREFIX;
    private final ConsoleCommandSender consoleCommandSender = Bukkit.getConsoleSender();

    public LoggerUtils(String prefix) {
        this.PREFIX = prefix;
    }
    public void logInfo(String message) {
       this.consoleCommandSender.sendMessage(String.format("%s%s %s", Constants.INFO_COLOR, PREFIX, message));
    }

    public void logWarning(String message) {
        this.consoleCommandSender.sendMessage(String.format("%s%s %s", Constants.WARNING_COLOR, PREFIX, message));
    }

}
