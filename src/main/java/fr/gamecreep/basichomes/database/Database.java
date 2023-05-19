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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static fr.gamecreep.basichomes.utils.PasswordUtils.hashPassword;

public class Database {
    private final BasicHomes plugin;
    private final String url;
    private final String username;
    private final String password;

    public Database(BasicHomes plugin, String jdbc, String username, String password) {
        this.plugin = plugin;
        this.url = jdbc;
        this.username = username;
        this.password = password;

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            Connection connection = getConnection();
            plugin.getPluginLogger().logInfo("Database connected !");
            connection.close();
        } catch (SQLException e) {
            plugin.getPluginLogger().logWarning("Could not connect to Database.");
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }

    public String getHomesTableName() {
        String tableName = "homes_" + plugin.generateServerUUID().toString().replace("-", "");
        return tableName;
    }

    public void createHomesTableIfNotExists() throws SQLException {
        Connection conn = getConnection();

        String tableName = getHomesTableName();
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "("
                + "homeID SERIAL NOT NULL PRIMARY KEY,"
                + "uuid TEXT NOT NULL,"
                + "homename TEXT NOT NULL,"
                + "x DOUBLE PRECISION NOT NULL,"
                + "y DOUBLE PRECISION NOT NULL,"
                + "z DOUBLE PRECISION NOT NULL,"
                + "world TEXT NOT NULL"
                + ")";

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.executeUpdate();
            statement.close();
            conn.close();
        }
    }
    public List<PlayerHome> getAllPlayerHomes(@NonNull Player player) throws SQLException {
        String uuid = player.getUniqueId().toString();
        String sql = "SELECT * FROM " + getHomesTableName() + " WHERE uuid = '" + uuid + "'";
        ResultSet resultSet;
        Connection connection = getConnection();

        Statement statement = connection.createStatement();
        resultSet = statement.executeQuery(sql);
        List<PlayerHome> playerHomeList = new ArrayList<>();

        while (resultSet.next()) {
            String homename = resultSet.getString("homename");
            double x = resultSet.getDouble("x");
            double y = resultSet.getDouble("y");
            double z = resultSet.getDouble("z");
            String strWorld = resultSet.getString("world");

            World world = Bukkit.getWorld(strWorld);
            Location loc = new Location(world, x, y, z);

            PlayerHome home = new PlayerHome(homename, player, loc);
            playerHomeList.add(home);
        }
        resultSet.close();
        statement.close();
        connection.close();
        return playerHomeList;
    }

    public void addHome(@NonNull PlayerHome home) throws SQLException {
       String sql = "INSERT INTO " + getHomesTableName() + " (uuid, homename, x, y, z, world) VALUES ("
               + "'" + home.getUuid() + "',"
               + "'" + home.getHomeName() + "',"
               + "'" + home.getX() + "',"
               + "'" + home.getY() + "',"
               + "'" + home.getZ() + "',"
               + "'" + home.getWorld() + "')";

       Connection connection = getConnection();
       PreparedStatement statement = connection.prepareStatement(sql);
       statement.execute();
       connection.close();
    }

    public void removeHome(@NonNull PlayerHome home) throws SQLException {
        String sql = "DELETE FROM " + getHomesTableName() + " WHERE "
                + "uuid = '" + home.getUuid() + "' AND "
                + "homename = '" + home.getHomeName() + "'";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.execute();
        connection.close();
    }

    public PlayerAccount createPlayerAccount(Player player) throws SQLException {
        Connection conn = getConnection();
        String password = PasswordUtils.generatePassword();
        String hashedPassword = hashPassword(password);
        String rank;

        if (player.hasPermission("basichomes.op")) rank = "admin";
        else rank = "user";

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO accounts (userId, password, rank) VALUES (?, ?, ?) RETURNING accountId");
        stmt.setString(1, player.getUniqueId().toString());
        stmt.setString(2, hashedPassword);
        stmt.setString(3, rank);

        int accountId;

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                accountId = rs.getInt(1);
            } else {
                throw new SQLException("Insert failed, no rows affected.");
            }
        }

        addServerToPlayer(player);

        PlayerAccount acc = new PlayerAccount(accountId, password, rank);
        stmt.close();
        conn.close();
        return acc;
    }

    public PlayerAccount getPlayerAccount(Player player) throws SQLException {
        Connection conn = getConnection();
        String uuid = player.getUniqueId().toString();
        PlayerAccount acc;

        String sql = String.format("SELECT * FROM accounts WHERE userId = '%s'", uuid);
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            acc = new PlayerAccount(rs.getInt(1));
        } else {
            acc = null;
        }
        stmt.close();
        conn.close();
        return acc;
    }

    public void deleteAccount(Player player) throws SQLException {
        String uuid = player.getUniqueId().toString();

        deleteAccountServersDB(uuid);
        deleteSessionsDB(uuid);
        deleteAccountDB(uuid);
    }

    public void deleteAccountServersDB(String uuid) throws SQLException {
        Connection conn = getConnection();
        int accountId = getAccountIdFromUUID(uuid);

        String sql = String.format("DELETE FROM account_servers WHERE account_id = '%s'", accountId);
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.execute();

        stmt.close();

        conn.close();
    }

    public void deleteSessionsDB(String uuid) throws SQLException {
        Connection conn = getConnection();
        int accountId = getAccountIdFromUUID(uuid);

        String sql = String.format("DELETE FROM sessions WHERE user_id = '%s'", accountId);
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.execute();

        stmt.close();

        conn.close();
    }

    public void deleteAccountDB(String uuid) throws SQLException {
        Connection conn = getConnection();

        String sql = String.format("DELETE FROM accounts WHERE userId = '%s'", uuid);
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.execute();

        stmt.close();
        conn.close();
    }

    public PlayerAccount resetAccountPassword(Player player) throws SQLException {
        Connection conn = getConnection();
        String password = PasswordUtils.generatePassword();
        String hashedPassword = hashPassword(password);
        String rank;

        if (player.hasPermission(new Permission("basichomes.op"))) rank = "admin";
        else rank = "user";

        PreparedStatement stmt = conn.prepareStatement(String.format("UPDATE accounts SET password = '%s' RETURNING accountId", hashedPassword));
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

    public void addServerToPlayer(Player player) throws SQLException {
        Connection conn = getConnection();
        String uuid = player.getUniqueId().toString();
        int accountId = getAccountIdFromUUID(uuid);
        String serverId = plugin.generateServerUUID().toString();

        String sql = "INSERT INTO account_servers (account_id, server_id) VALUES (?, ?) ON CONFLICT DO NOTHING;";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, accountId);
        stmt.setString(2, serverId);

        stmt.execute();
        stmt.close();
        conn.close();
    }

    public boolean serverAlreadyRegistered(Player player) throws SQLException {
        Connection conn = getConnection();
        String uuid = player.getUniqueId().toString();
        int rowCount = 0;

        String sql = String.format("SELECT * FROM account_servers WHERE account_id = %s AND server_id = '%s'", getAccountIdFromUUID(uuid), plugin.generateServerUUID());
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            rowCount++;
        }

        return rowCount != 0;
    }

    public int getAccountIdFromUUID(String uuid) throws SQLException {
        Connection conn = getConnection();
        int accountId;

        String sql = String.format("SELECT accountid FROM accounts WHERE userid = '%s'", uuid);
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            accountId = rs.getInt("accountid");
        } else {
            throw new SQLException("Could not get account ID");
        }
        stmt.close();
        conn.close();
        return accountId;
    }
}
