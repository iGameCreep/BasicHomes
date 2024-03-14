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
import fr.gamecreep.basichomes.config.SubConfig;
import fr.gamecreep.basichomes.events.MenuEvents;
import fr.gamecreep.basichomes.files.DataHandler;
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
    private final DataHandler homeHandler = new DataHandler(this, "homes.json");
    private final DataHandler warpHandler = new DataHandler(this, "warps.json");
    private final PluginConfig pluginConfig = new PluginConfig();

    @Override
    public void onEnable() {
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
        if (this.pluginConfig.getHomesConfig().isEnabled()) {
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

        if (this.pluginConfig.getWarpsConfig().isEnabled()) {
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
        super.getServer().getPluginManager().registerEvents(new MenuEvents(this), this);

        this.pluginLogger.logInfo("Events loaded !");
    }

    private void loadConfig() {
        saveDefaultConfig();
        FileConfiguration configFile = super.getConfig();

        SubConfig homesConfig = new SubConfig();
        homesConfig.setEnabled(configFile.getBoolean("homes.enabled"));
        homesConfig.setTeleportAfterSeconds(configFile.getInt("homes.teleportAfterSeconds"));
        homesConfig.setDelayTeleport(homesConfig.getTeleportAfterSeconds() == 0);

        SubConfig warpsConfig = new SubConfig();
        warpsConfig.setEnabled(configFile.getBoolean("warps.enabled"));
        warpsConfig.setTeleportAfterSeconds(configFile.getInt("warps.teleportAfterSeconds"));
        warpsConfig.setDelayTeleport(warpsConfig.getTeleportAfterSeconds() == 0);

        this.pluginConfig.setHomesConfig(homesConfig);
        this.pluginConfig.setWarpsConfig(warpsConfig);
    }
}
