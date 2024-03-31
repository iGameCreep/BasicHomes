package fr.gamecreep.basichomes;

import fr.gamecreep.basichomes.commands.config.ConfigCommand;
import fr.gamecreep.basichomes.commands.homes.*;
import fr.gamecreep.basichomes.commands.warps.*;
import fr.gamecreep.basichomes.config.PluginConfig;
import fr.gamecreep.basichomes.entities.enums.ConfigElement;
import fr.gamecreep.basichomes.files.MigrationsVerifier;
import fr.gamecreep.basichomes.files.PositionDataHandler;
import fr.gamecreep.basichomes.menus.home.HomeMenuFactory;
import fr.gamecreep.basichomes.menus.warp.WarpMenuFactory;
import fr.gamecreep.basichomes.events.TeleportEvents;
import fr.gamecreep.basichomes.utils.ChatUtils;
import fr.gamecreep.basichomes.utils.LoggerUtils;
import fr.gamecreep.basichomes.utils.TeleportUtils;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@Getter
public final class BasicHomes extends JavaPlugin {
    private final LoggerUtils pluginLogger = new LoggerUtils(String.format("[%s]", this.getDescription().getPrefix()));
    private final ChatUtils chatUtils = new ChatUtils();
    private final PositionDataHandler homeHandler = new PositionDataHandler(this, "homes.json");
    private final PositionDataHandler warpHandler = new PositionDataHandler(this, "warps.json");
    private final PluginConfig pluginConfig = new PluginConfig();
    private final HomeMenuFactory homeMenuFactory = new HomeMenuFactory();
    private final WarpMenuFactory warpMenuFactory = new WarpMenuFactory();
    private final MigrationsVerifier migrationsVerifier = new MigrationsVerifier(this);
    private TeleportUtils teleportUtils;

    @Override
    public void onEnable() {
        this.migrationsVerifier.verifyMigrations();
        loadConfig();
        this.teleportUtils = new TeleportUtils(this);
        loadCommands();
        loadEvents();

        this.pluginLogger.logInfo("Plugin successfully loaded !");
    }

    @Override
    public void onDisable() {
        this.pluginLogger.logInfo("Plugin successfully stopped !");
    }

    private void loadCommands() {
        Objects.requireNonNull(super.getCommand("config")).setExecutor(new ConfigCommand(this));

        List<String> homeCommands = List.of("homes", "sethome", "delhome", "home", "delhomeof", "homesof");
        List<String> warpCommands = List.of("warps", "setwarp", "delwarp", "warp");

        for (String cmd : homeCommands) {
            Objects.requireNonNull(super.getCommand(cmd)).setExecutor(new HomesHandler(this));
        }
        for (String cmd : warpCommands) {
            Objects.requireNonNull(super.getCommand(cmd)).setExecutor(new WarpsHandler(this));
        }

        this.pluginLogger.logInfo("Commands loaded !");
    }

    private void loadEvents() {
        super.getServer().getPluginManager().registerEvents(new TeleportEvents(this), this);
        this.getServer().getPluginManager().registerEvents(this.homeMenuFactory, this);
        this.getServer().getPluginManager().registerEvents(this.warpMenuFactory, this);

        this.pluginLogger.logInfo("Events loaded !");
    }

    private void loadConfig() {
        saveDefaultConfig();
        FileConfiguration configFile = super.getConfig();
        Map<ConfigElement, Object> config = this.pluginConfig.getConfig();

        for (ConfigElement element : ConfigElement.values()) {
            if (!configFile.contains(element.getPath())) {
                configFile.addDefault(element.getPath(), element.getDefaultValue());
            }
            config.put(element, configFile.get(element.getPath()));
        }

        this.pluginConfig.setConfig(config);
        saveConfig();
    }

    public void updateConfig(ConfigElement element, Object newValue) {
        this.pluginConfig.getConfig().remove(element);
        this.pluginConfig.getConfig().put(element, newValue);
        getConfig().set(element.getPath(), newValue);
        saveConfig();
        reloadConfig();
    }
}
