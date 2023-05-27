package fr.gamecreep.basichomes;

import fr.gamecreep.basichomes.entities.accounts.PlayerAccount;
import fr.gamecreep.basichomes.entities.commands.*;
import fr.gamecreep.basichomes.database.Database;
import fr.gamecreep.basichomes.entities.events.MenuEvents;
import fr.gamecreep.basichomes.entities.homes.PlayerHome;
import fr.gamecreep.basichomes.utils.ChatUtils;
import fr.gamecreep.basichomes.utils.HomesUtils;
import fr.gamecreep.basichomes.utils.LoggerUtils;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public final class BasicHomes extends JavaPlugin {
    private final HashSet<PlayerHome> playerHomes = new HashSet<>();
    private Database db;
    private final LoggerUtils pluginLogger = new LoggerUtils("[" + getDescription().getPrefix() + "] " );
    private final ChatUtils chatUtils = new ChatUtils();
    private final HomesUtils homesUtils = new HomesUtils(this);

    @Override
    public void onEnable() {
        loadCommands();
        loadEvents();
        loadConfig();
        generateServerUUID();
        loadTables();

        pluginLogger.logInfo("Plugin successfully loaded !");
    }

    @Override
    public void onDisable() {
        pluginLogger.logInfo("Plugin successfully stopped !");
    }

    public void loadTables() {
        try {
            getDb().createHomesTableIfNotExists();
        } catch (SQLException e) {
            getLogger().warning("Could not load the database tables.");
            throw new RuntimeException(e);
        }
    }

    public UUID generateServerUUID() {
        File configFile = new File(getDataFolder(), "server.yml");
        UUID serverUUID;

        if (configFile.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
            String idInConfig = config.getString("server-id");
            serverUUID = UUID.fromString(idInConfig);
        } else {
            // Generate a new UUID if the configuration file doesn't exist
            serverUUID = UUID.randomUUID();
            FileConfiguration config = new YamlConfiguration();
            config.set("server-id", serverUUID.toString());
            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return serverUUID;
    }

    public void loadCommands() {
        Objects.requireNonNull(getCommand("homes")).setExecutor(new Homes(this));
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SetHome(this));
        Objects.requireNonNull(getCommand("delhome")).setExecutor(new DelHome(this));
        Objects.requireNonNull(getCommand("home")).setExecutor(new Home(this));
        Objects.requireNonNull(getCommand("account")).setExecutor(new Account(this));

        pluginLogger.logInfo("Commands loaded !");
    }

    public void loadEvents() {
        getServer().getPluginManager().registerEvents(new MenuEvents(this), this);

        pluginLogger.logInfo("Events loaded !");
    }

    public void loadDatabase(String db_jdbc, String db_user, String db_password) {
        this.db = new Database(this, db_jdbc, db_user, db_password);

        pluginLogger.logInfo("Database loaded !");
    }

    public void loadConfig() {
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        String db_jdbc = config.getString("database-prod.jdbc-url");
        String db_user = config.getString("database-prod.username");
        String db_password = config.getString("database-prod.password");

        loadDatabase(db_jdbc, db_user, db_password);
    }

    public void createHome(String name, @NonNull Player player, @NonNull Location loc) {
        PlayerHome home = new PlayerHome(name, player, loc);
        try {
            db.addHome(home);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeHome(@NonNull PlayerHome home) {
        try {
            db.removeHome(home);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeHomeByName(@NonNull Player player, String name) {
        PlayerHome home = getHomeByName(player, name);
        removeHome(home);
    }

    public List<PlayerHome> getAllPlayerHomes(@NonNull Player player) {
        List<PlayerHome> homes;
        try {
            homes = db.getAllPlayerHomes(player);
        } catch (SQLException err) {
            throw new Error(err);
        }
        return homes;
    }

    public PlayerHome getHomeByName(@NonNull Player owner, String homeName) {
        for (PlayerHome home : getAllPlayerHomes(owner)) {
            if (home.getHomeName().equalsIgnoreCase(homeName)) return home;
        }
        throw new Error("Unknown home");
    }

    public PlayerAccount createPlayerAccount(@NonNull Player player) {
        PlayerAccount acc;
        try {
            acc = getDb().createPlayerAccount(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return acc;
    }

    public PlayerAccount getPlayerAccount(@NonNull Player player) {
        PlayerAccount acc;
        try {
            acc = getDb().getPlayerAccount(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return acc;
    }

    public void addServerToPlayer(Player player) {
        try {
            getDb().addServerToPlayer(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PlayerAccount resetAccountPassword(Player player) {
        PlayerAccount acc;
        try {
            acc = getDb().resetAccountPassword(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return acc;
    }

    public void deletePlayerAccount(Player player) {
        try {
            getDb().deleteAccount(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean serverAlreadyRegistered(Player player) {
        boolean registered;
        try {
            registered = this.getDb().serverAlreadyRegistered(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return registered;
    }
}
