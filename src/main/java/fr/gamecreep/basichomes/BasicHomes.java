package fr.gamecreep.basichomes;

import fr.gamecreep.basichomes.entities.commands.*;
import fr.gamecreep.basichomes.entities.events.MenuEvents;
import fr.gamecreep.basichomes.entities.homes.PlayerHome;
import fr.gamecreep.basichomes.files.DataHandler;
import fr.gamecreep.basichomes.utils.ChatUtils;
import fr.gamecreep.basichomes.utils.HomesUtils;
import fr.gamecreep.basichomes.utils.LoggerUtils;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@Getter
public final class BasicHomes extends JavaPlugin {
    private final LoggerUtils pluginLogger = new LoggerUtils(String.format("[%s]", this.getDescription().getPrefix()));
    private final ChatUtils chatUtils = new ChatUtils();
    private final HomesUtils homesUtils = new HomesUtils(this);
    private final DataHandler dataHandler = new DataHandler(this);

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
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SetHome(this));
        Objects.requireNonNull(getCommand("delhome")).setExecutor(new DelHome(this));
        Objects.requireNonNull(getCommand("home")).setExecutor(new Home(this));

        this.pluginLogger.logInfo("Commands loaded !");
    }

    public void loadEvents() {
        getServer().getPluginManager().registerEvents(new MenuEvents(this), this);

        this.pluginLogger.logInfo("Events loaded !");
    }

    public void createHome(@NonNull PlayerHome home) {
        this.dataHandler.createHome(home);
    }

    public void removeHome(@NonNull PlayerHome home) {
        this.dataHandler.deleteHome(home);
    }

    public List<PlayerHome> getAllPlayerHomes(@NonNull Player player) {
        return this.dataHandler.getAllPlayerHomes(player);
    }

    public PlayerHome getHomeByName(@NonNull Player player, String name) {
        return this.dataHandler.getHomeByName(player, name);
    }
}
