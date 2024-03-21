package fr.gamecreep.basichomes;

import fr.gamecreep.basichomes.commands.create.CreateHome;
import fr.gamecreep.basichomes.commands.create.CreateWarp;
import fr.gamecreep.basichomes.commands.delete.DeleteHome;
import fr.gamecreep.basichomes.commands.delete.DeleteHomeOf;
import fr.gamecreep.basichomes.commands.delete.DeleteWarp;
import fr.gamecreep.basichomes.commands.disabled.HomesDisabled;
import fr.gamecreep.basichomes.commands.disabled.WarpsDisabled;
import fr.gamecreep.basichomes.commands.get.GetHomes;
import fr.gamecreep.basichomes.commands.get.GetHomesOf;
import fr.gamecreep.basichomes.commands.get.GetWarps;
import fr.gamecreep.basichomes.commands.teleport.TeleportHome;
import fr.gamecreep.basichomes.commands.teleport.TeleportWarp;
import fr.gamecreep.basichomes.config.PluginConfig;
import fr.gamecreep.basichomes.files.MigrationsVerifier;
import fr.gamecreep.basichomes.files.PositionDataHandler;
import fr.gamecreep.basichomes.menus.home.HomeMenuFactory;
import fr.gamecreep.basichomes.menus.warp.WarpMenuFactory;
import fr.gamecreep.basichomes.utils.ChatUtils;
import fr.gamecreep.basichomes.utils.LoggerUtils;
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

    @Override
    public void onEnable() {
        this.migrationsVerifier.verifyMigrations();
        loadConfig();
        loadCommands();
        loadEvents();

        this.pluginLogger.logInfo("Plugin successfully loaded !");
    }

    @Override
    public void onDisable() {
        this.pluginLogger.logInfo("Plugin successfully stopped !");
    }

    private void loadCommands() {
        if (this.pluginConfig.isHomesEnabled()) {
            Objects.requireNonNull(super.getCommand("homes")).setExecutor(new GetHomes(this));
            Objects.requireNonNull(super.getCommand("sethome")).setExecutor(new CreateHome(this));
            Objects.requireNonNull(super.getCommand("delhome")).setExecutor(new DeleteHome(this));
            Objects.requireNonNull(super.getCommand("home")).setExecutor(new TeleportHome(this));
            Objects.requireNonNull(super.getCommand("delhomeof")).setExecutor(new DeleteHomeOf(this));
            Objects.requireNonNull(super.getCommand("homesof")).setExecutor(new GetHomesOf(this));
        } else {
            List<String> homeCommands = List.of("homes", "sethome", "delhome", "home", "delhomeof", "homesof");
            HomesDisabled disabledCommand = new HomesDisabled(this);

            for (String cmd : homeCommands) {
                Objects.requireNonNull(super.getCommand(cmd)).setExecutor(disabledCommand);
            }
        }

        if (this.pluginConfig.isWarpsEnabled()) {
            Objects.requireNonNull(super.getCommand("warps")).setExecutor(new GetWarps(this));
            Objects.requireNonNull(super.getCommand("setwarp")).setExecutor(new CreateWarp(this));
            Objects.requireNonNull(super.getCommand("delwarp")).setExecutor(new DeleteWarp(this));
            Objects.requireNonNull(super.getCommand("warp")).setExecutor(new TeleportWarp(this));
        } else {
            List<String> homeCommands = List.of("warps", "setwarp", "delwarp", "warp");
            WarpsDisabled disabledCommand = new WarpsDisabled(this);

            for (String cmd : homeCommands) {
                Objects.requireNonNull(super.getCommand(cmd)).setExecutor(disabledCommand);
            }
        }

        this.pluginLogger.logInfo("Commands loaded !");
    }

    private void loadEvents() {
        this.getServer().getPluginManager().registerEvents(this.homeMenuFactory, this);
        this.getServer().getPluginManager().registerEvents(this.warpMenuFactory, this);

        this.pluginLogger.logInfo("Events loaded !");
    }

    private void loadConfig() {
        saveDefaultConfig();
        FileConfiguration configFile = super.getConfig();

        boolean homesEnabled = configFile.getBoolean("homes.enabled");
        boolean warpsEnabled = configFile.getBoolean("warps.enabled");

        this.pluginConfig.setHomesEnabled(homesEnabled);
        this.pluginConfig.setWarpsEnabled(warpsEnabled);
    }
}
