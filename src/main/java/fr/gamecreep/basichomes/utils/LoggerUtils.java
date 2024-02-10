package fr.gamecreep.basichomes.utils;

import org.bukkit.Bukkit;

public class LoggerUtils {
    private final String PREFIX;

    public LoggerUtils(String prefix) {
        this.PREFIX = prefix;
    }
    public void logInfo(String message) {
        String toLog = String.format("%s %s", PREFIX, message);
        Bukkit.getServer().getLogger().info(toLog);
    }

    public void logWarning(String message) {
        String toLog = String.format("%s %s", PREFIX, message);
        Bukkit.getServer().getLogger().warning(toLog);
    }


}
