package fr.gamecreep.basichomes;

import fr.gamecreep.basichomes.commands.config.ConfigCommand;
import fr.gamecreep.basichomes.commands.homes.*;
import fr.gamecreep.basichomes.commands.warps.*;
import fr.gamecreep.basichomes.config.PluginConfig;
import fr.gamecreep.basichomes.config.enums.ConfigElement;
import fr.gamecreep.basichomes.exceptions.BasicHomesException;
import fr.gamecreep.basichomes.files.MigrationsVerifier;
import fr.gamecreep.basichomes.files.PositionDataHandler;
import fr.gamecreep.basichomes.menus.home.HomeMenuFactory;
import fr.gamecreep.basichomes.menus.warp.WarpMenuFactory;
import fr.gamecreep.basichomes.events.TeleportEvents;
import fr.gamecreep.basichomes.utils.LoggerUtils;
import fr.gamecreep.basichomes.utils.TeleportUtils;
import fr.gamecreep.basichomes.utils.Updater;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@Getter
public final class BasicHomes extends JavaPlugin {

    private final PositionDataHandler positionDataHandler = new PositionDataHandler(this, "data.json");
    private final PluginConfig pluginConfig = new PluginConfig();
    private final HomeMenuFactory homeMenuFactory = new HomeMenuFactory();
    private final WarpMenuFactory warpMenuFactory = new WarpMenuFactory();
    private final MigrationsVerifier migrationsVerifier = new MigrationsVerifier(this);
    private final TeleportUtils teleportUtils = new TeleportUtils(this);

    @Override
    public void onEnable() {
        this.migrationsVerifier.verifyMigrations();
        this.loadConfig();
        this.loadCommands();
        this.loadEvents();
        this.loadMetrics();
        this.checkForUpdates();

        LoggerUtils.logInfo("Plugin successfully loaded !");
    }

    @Override
    public void onDisable() {
        LoggerUtils.logInfo("Plugin successfully stopped !");
    }

    private void loadCommands() {
        Objects.requireNonNull(super.getCommand("config")).setExecutor(new ConfigCommand(this));

        final List<String> homeCommands = List.of("homes", "sethome", "delhome", "edithome", "home", "delhomeof", "homesof");
        final List<String> warpCommands = List.of("warps", "setwarp", "delwarp", "editwarp", "warp");

        for (final String cmd : homeCommands) {
            Objects.requireNonNull(super.getCommand(cmd)).setExecutor(new HomesHandler(this));
        }
        for (final String cmd : warpCommands) {
            Objects.requireNonNull(super.getCommand(cmd)).setExecutor(new WarpsHandler(this));
        }

        LoggerUtils.logInfo("Commands loaded !");
    }

    private void loadEvents() {
        super.getServer().getPluginManager().registerEvents(new TeleportEvents(this), this);
        this.getServer().getPluginManager().registerEvents(this.homeMenuFactory, this);
        this.getServer().getPluginManager().registerEvents(this.warpMenuFactory, this);

        LoggerUtils.logInfo("Events loaded !");
    }

    private void loadConfig() {
        saveDefaultConfig();
        final FileConfiguration configFile = super.getConfig();
        final Map<ConfigElement, Object> config = this.pluginConfig.getConfig();

        for (final ConfigElement element : ConfigElement.values()) {
            if (!configFile.contains(element.getPath())) {
                config.put(element, element.getDefaultValue());
            } else {
                config.put(element, configFile.get(element.getPath()));
            }
        }

        this.pluginConfig.setConfig(config);
        saveConfig();
    }

    private void loadMetrics() {
        try {
            final Metrics metrics = new Metrics(this, Constants.BSTATS_PLUGIN_ID);

            metrics.addCustomChart(new SimplePie("using_warps",
                    () -> Boolean.toString((Boolean) this.pluginConfig.getConfig().getOrDefault(ConfigElement.WARPS_ENABLED, false))
            ));

            this.getLogger().info("Metrics (bStats) successfully loaded !");
        } catch (Exception e) {
            this.getLogger().warning("Failed to register plugin metrics: " + e.getMessage());
        }
    }

    private void checkForUpdates() {
        LoggerUtils.logInfo("Checking for updates...");
        try {
            final Updater updater = new Updater();
            final Updater.UpdateData data = updater.checkForUpdates();

            if (data == null) {
                LoggerUtils.logInfo("Plugin is up to date.");
            } else {
                LoggerUtils.logWarning(String.format("An update is available: %s. Download it at %s", data.getLatestVersion(), data.getDownloadUrl()));
            }
        } catch (BasicHomesException e) {
            LoggerUtils.logWarning("Unable to check for latest versions: " + e.getMessage());
        }
    }

    public void updateConfig(final ConfigElement element, final Object newValue) {
        this.pluginConfig.getConfig().remove(element);
        this.pluginConfig.getConfig().put(element, newValue);
        getConfig().set(element.getPath(), newValue);
        saveConfig();
        reloadConfig();
    }
}
