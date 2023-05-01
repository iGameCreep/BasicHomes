package fr.gamecreep.basichomes.utils;

import org.bukkit.Bukkit;

public class LoggerUtils {
    private final String PREFIX;

    public LoggerUtils(String prefix) {
        this.PREFIX = prefix;
    }
    public void logInfo(String message) {
        Bukkit.getServer().getLogger().info(PREFIX + message);
    }

    public void logWarning(String message) {
        Bukkit.getServer().getLogger().warning(PREFIX + message);
    }


}
