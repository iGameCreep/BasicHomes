package fr.gamecreep.basichomes;

import fr.gamecreep.basichomes.entities.accounts.PlayerAccount;
import fr.gamecreep.basichomes.entities.commands.*;
import fr.gamecreep.basichomes.database.Database;
import fr.gamecreep.basichomes.entities.events.MenuEvents;
import fr.gamecreep.basichomes.entities.homes.PlayerHome;
import fr.gamecreep.basichomes.files.DataStore;
import fr.gamecreep.basichomes.utils.ChatUtils;
import fr.gamecreep.basichomes.utils.HomesUtils;
import fr.gamecreep.basichomes.utils.LoggerUtils;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.*;

@Getter
public final class BasicHomes extends JavaPlugin {
    private final HashSet<PlayerHome> playerHomes = new HashSet<>();
    private Database db;
    private final LoggerUtils pluginLogger = new LoggerUtils(String.format("[%s]", this.getDescription().getPrefix()));
    private final ChatUtils chatUtils = new ChatUtils();
    private final HomesUtils homesUtils = new HomesUtils(this);
    private final DataStore dataStore = new DataStore(this);

    @Override
    public void onEnable() {
        loadCommands();
        loadEvents();
        loadConfig();
        loadTables();

        loadFiles();

        this.pluginLogger.logInfo("Plugin successfully loaded !");
    }

    @Override
    public void onDisable() {
        this.pluginLogger.logInfo("Plugin successfully stopped !");
    }

    private void loadFiles() {
        this.dataStore.saveData(new ArrayList<>(getPlayerHomes()));
    }

    public void loadTables() {
        try {
            db.createHomesTableIfNotExists();
            db.createAccountsTableIfNotExists();
            db.createSessionsTableIfNotExists();
        } catch (SQLException e) {
            this.getLogger().warning("Could not load the database tables.");
        }
    }

    public void loadCommands() {
        Objects.requireNonNull(getCommand("homes")).setExecutor(new Homes(this));
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SetHome(this));
        Objects.requireNonNull(getCommand("delhome")).setExecutor(new DelHome(this));
        Objects.requireNonNull(getCommand("home")).setExecutor(new Home(this));
        Objects.requireNonNull(getCommand("account")).setExecutor(new Account(this));

        this.pluginLogger.logInfo("Commands loaded !");
    }

    public void loadEvents() {
        getServer().getPluginManager().registerEvents(new MenuEvents(this), this);

        this.pluginLogger.logInfo("Events loaded !");
    }

    public void loadConfig() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        String jdbc = config.getString("database.jdbc-url");
        String user = config.getString("database.username");
        String password = config.getString("database.password");

        this.db = new Database(this, jdbc, user, password);
        this.pluginLogger.logInfo("Database loaded !");
    }

    public void createHome(String name, @NonNull Player player, @NonNull Location loc) {
        PlayerHome home = new PlayerHome(name, player, loc);
        try {
            this.db.addHome(home);
        } catch (SQLException e) {
            this.pluginLogger.logInfo(String.format(
                    "Could not create the home %s. By %s. At X: %f Y: %f Z: %f World: %s",
                    home.getHomeName(),
                    player.getName(),
                    loc.getX(),
                    loc.getY(),
                    loc.getZ(),
                    loc.getWorld()
            ));
        }
    }

    public void removeHome(@NonNull PlayerHome home) {
        try {
            this.db.removeHome(home);
        } catch (SQLException e) {
            this.pluginLogger.logInfo(String.format(
                    "Could not create the home %s. By %s. At X: %f Y: %f Z: %f World: %s",
                    home.getHomeName(),
                    Objects.requireNonNull(getServer().getPlayer(UUID.fromString(home.getUuid()))).getName(),
                    home.getX(),
                    home.getY(),
                    home.getZ(),
                    home.getWorld()
            ));
        }
    }

    public List<PlayerHome> getAllPlayerHomes(@NonNull Player player) {
        List<PlayerHome> homes = Collections.emptyList();
        try {
            homes = this.db.getAllPlayerHomes(player);
        } catch (SQLException err) {
            this.pluginLogger.logInfo(String.format("Could not all homes of player %s", player.getName()));
        }
        return homes;
    }

    public PlayerHome getHomeByName(@NonNull Player player, String homeName) {
        for (PlayerHome home : getAllPlayerHomes(player)) {
            if (home.getHomeName().equalsIgnoreCase(homeName)) return home;
        }
        this.pluginLogger.logInfo(String.format("Could not find home '%s' of player %s", homeName, player.getName()));
        return null;
    }

    public PlayerAccount createPlayerAccount(@NonNull Player player) {
        PlayerAccount acc;
        try {
            acc = this.db.createPlayerAccount(player);
        } catch (Exception e) {
            this.pluginLogger.logInfo(String.format("Could not create the account for player %s", player.getName()));
            return null;
        }

        return acc;
    }

    public PlayerAccount getPlayerAccount(@NonNull Player player) {
        PlayerAccount acc;
        try {
            acc = this.db.getPlayerAccount(player);
        } catch (SQLException e) {
            this.pluginLogger.logInfo(String.format("Could not find account of player %s", player.getName()));
            return null;
        }
        return acc;
    }

    public PlayerAccount resetAccountPassword(Player player) {
        PlayerAccount acc;
        try {
            acc = this.db.resetAccountPassword(player);
        } catch (Exception e) {
            this.pluginLogger.logInfo(String.format("Could not reset password of account of player %s", player.getName()));
            return null;
        }
        return acc;
    }

    public void deletePlayerAccount(Player player) {
        try {
            this.db.deleteAccount(player);
        } catch (SQLException e) {
            this.pluginLogger.logInfo(String.format("Could not delete account of player %s", player.getName()));
        }
    }
}
