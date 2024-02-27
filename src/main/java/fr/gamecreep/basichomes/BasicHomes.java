package fr.gamecreep.basichomes;

import fr.gamecreep.basichomes.entities.commands.create.CreateHome;
import fr.gamecreep.basichomes.entities.commands.create.CreateWarp;
import fr.gamecreep.basichomes.entities.commands.delete.DeleteHome;
import fr.gamecreep.basichomes.entities.commands.delete.DeleteWarp;
import fr.gamecreep.basichomes.entities.commands.get.Homes;
import fr.gamecreep.basichomes.entities.commands.get.Warps;
import fr.gamecreep.basichomes.entities.commands.teleport.TeleportHome;
import fr.gamecreep.basichomes.entities.commands.teleport.TeleportWarp;
import fr.gamecreep.basichomes.files.DataHandler;
import fr.gamecreep.basichomes.menus.HomeMenu;
import fr.gamecreep.basichomes.menus.WarpMenu;
import fr.gamecreep.basichomes.utils.ChatUtils;
import fr.gamecreep.basichomes.utils.LoggerUtils;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@Getter
public final class BasicHomes extends JavaPlugin {
    private final LoggerUtils pluginLogger = new LoggerUtils(String.format("[%s]", this.getDescription().getPrefix()));
    private final ChatUtils chatUtils = new ChatUtils();
    private final DataHandler homeHandler = new DataHandler(this, "homes.json");
    private final DataHandler warpHandler = new DataHandler(this, "warps.json");

    @Override
    public void onEnable() {
        loadCommands();
        loadEvents();

        saveDefaultConfig();

        this.pluginLogger.logInfo("Plugin successfully loaded !");
    }

    @Override
    public void onDisable() {
        this.pluginLogger.logInfo("Plugin successfully stopped !");
    }

    public void loadCommands() {
        Objects.requireNonNull(getCommand("homes")).setExecutor(new Homes(this));
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new CreateHome(this));
        Objects.requireNonNull(getCommand("delhome")).setExecutor(new DeleteHome(this));
        Objects.requireNonNull(getCommand("home")).setExecutor(new TeleportHome(this));

        Objects.requireNonNull(getCommand("warps")).setExecutor(new Warps(this));
        Objects.requireNonNull(getCommand("setwarp")).setExecutor(new CreateWarp(this));
        Objects.requireNonNull(getCommand("delwarp")).setExecutor(new DeleteWarp(this));
        Objects.requireNonNull(getCommand("warp")).setExecutor(new TeleportWarp(this));

        this.pluginLogger.logInfo("Commands loaded !");
    }

    public void loadEvents() {
        getServer().getPluginManager().registerEvents(new HomeMenu(this), this);
        getServer().getPluginManager().registerEvents(new WarpMenu(this), this);

        this.pluginLogger.logInfo("Events loaded !");
    }
}
