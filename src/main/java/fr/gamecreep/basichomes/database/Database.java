package fr.gamecreep.basichomes.database;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.accounts.PlayerAccount;
import fr.gamecreep.basichomes.entities.homes.PlayerHome;
import fr.gamecreep.basichomes.utils.PasswordUtils;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;

    public Database(BasicHomes plugin, String jdbc, String username, String password) {
        this.dbUrl = jdbc;
        this.dbUsername = username;
        this.dbPassword = password;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            plugin.getPluginLogger().logWarning("Could not load postgresql driver. Contact developers.");
        }
        try {
            Connection connection = getConnection();
            plugin.getPluginLogger().logInfo("Database connected !");
            connection.close();
        } catch (SQLException e) {
            plugin.getPluginLogger().logWarning("Could not connect to Database.");
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl, this.dbUsername, this.dbPassword);
    }

    public void createSessionsTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS sessions (" +
                "accountID INT NOT NULL," +
                "token TEXT NOT NULL UNIQUE," +
                "createdAt TIMESTAMP WITH TIME ZONE DEFAULT NOW()," +
                "expireAt TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW() + INTERVAL '30 minutes'" +
                ");";

        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.execute();
        }
    }

    public void createAccountsTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS accounts (" +
                "accountID SERIAL PRIMARY KEY," +
                "userID TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "rank TEXT NOT NULL" +
                ");";

        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.execute();
        }
    }

    public void createHomesTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS homes ("
                + "homeID SERIAL NOT NULL PRIMARY KEY,"
                + "uuid TEXT NOT NULL,"
                + "homename TEXT NOT NULL,"
                + "x DOUBLE PRECISION NOT NULL,"
                + "y DOUBLE PRECISION NOT NULL,"
                + "z DOUBLE PRECISION NOT NULL,"
                + "world TEXT NOT NULL"
                + ")";

        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.execute();
        }
    }
    public List<PlayerHome> getAllPlayerHomes(@NonNull Player player) throws SQLException {
        String sql = String.format("SELECT * FROM homes WHERE uuid = '%s'", player.getUniqueId());
        List<PlayerHome> playerHomeList = new ArrayList<>();

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String homeName = resultSet.getString("homename");
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double z = resultSet.getDouble("z");
                String strWorld = resultSet.getString("world");

                World world = Bukkit.getWorld(strWorld);
                Location loc = new Location(world, x, y, z);

                PlayerHome home = new PlayerHome(homeName, player, loc);
                playerHomeList.add(home);
            }
        }
        return playerHomeList;
    }

    public void addHome(@NonNull PlayerHome home) throws SQLException {
        String sql = "INSERT INTO homes (uuid, homename, x, y, z, world) VALUES ("
                + "'" + home.getUuid() + "',"
                + "'" + home.getHomeName() + "',"
                + "'" + home.getX() + "',"
                + "'" + home.getY() + "',"
                + "'" + home.getZ() + "',"
                + "'" + home.getWorld() + "')";

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        }
    }

    public void removeHome(@NonNull PlayerHome home) throws SQLException {
        String sql = "DELETE FROM homes WHERE "
                + "uuid = '" + home.getUuid() + "' AND "
                + "homename = '" + home.getHomeName() + "'";

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.execute();
        }
    }

    public PlayerAccount createPlayerAccount(Player player) throws NoSuchAlgorithmException, SQLException {
        String password = PasswordUtils.generatePassword();
        String hashedPassword = PasswordUtils.hashPassword(password);
        String rank = (player.hasPermission(new Permission("basichomes.op"))) ? "admin" : "user";

        String sql = String.format(
                "INSERT INTO accounts (userID, password, rank) VALUES ('%s', '%s', '%s') RETURNING accountId",
                player.getUniqueId(),
                hashedPassword,
                rank);

        int accountId;

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    accountId = rs.getInt(1);
                } else {
                    throw new SQLException(String.format("Could not create an account for player %s (UUID: %s)", player.getName(), player.getUniqueId()));
                }
            }
        }
        return new PlayerAccount(accountId, password, rank);
    }

    public PlayerAccount getPlayerAccount(Player player) throws SQLException {
        String uuid = player.getUniqueId().toString();
        PlayerAccount acc;

        String sql = String.format("SELECT * FROM accounts WHERE userID = '%s'", uuid);
        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                acc = new PlayerAccount(rs.getInt(1));
            } else {
                acc = null;
            }
        }
        return acc;
    }

    public void deleteAccount(Player player) throws SQLException {
        String uuid = player.getUniqueId().toString();

        deleteSessionsDB(uuid);
        deleteAccountDB(uuid);
    }

    public void deleteSessionsDB(String uuid) throws SQLException {
        Connection conn = getConnection();
        int accountId = getAccountIdFromUUID(uuid);

        String sql = String.format("DELETE FROM sessions WHERE accountID = '%s'", accountId);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        }

        conn.close();
    }

    public void deleteAccountDB(String uuid) throws SQLException {
        Connection conn = getConnection();

        String sql = String.format("DELETE FROM accounts WHERE userID = '%s'", uuid);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        }

        conn.close();
    }

    public PlayerAccount resetAccountPassword(Player player) throws SQLException, NoSuchAlgorithmException {
        Connection conn = getConnection();
        String password = PasswordUtils.generatePassword();
        String hashedPassword = PasswordUtils.hashPassword(password);
        String uuid = player.getUniqueId().toString();
        String rank = (player.hasPermission(new Permission("basichomes.op"))) ? "admin" : "user";

        PreparedStatement stmt = conn.prepareStatement(String.format("UPDATE accounts SET password = '%s' WHERE userID = '%s' RETURNING accountID", hashedPassword, uuid));
        int accountId;

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                accountId = rs.getInt(1);
            } else {
                throw new SQLException("Update failed, no rows affected.");
            }
        }

        PlayerAccount acc = new PlayerAccount(accountId, password, rank);
        stmt.close();
        conn.close();
        return acc;
    }

    public int getAccountIdFromUUID(String uuid) throws SQLException {
        Connection conn = getConnection();
        int accountId;
        String sql = String.format("SELECT accountID FROM accounts WHERE userID = '%s'", uuid);

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                accountId = rs.getInt("accountID");
            } else {
                throw new SQLException("Could not get account ID");
            }
        }

        conn.close();
        return accountId;
    }
}