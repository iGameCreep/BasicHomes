package fr.gamecreep.basichomes.database;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.homes.PlayerHome;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public void buildDatabase() throws SQLException {
        try {
            Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("DROP TABLE homes");
            stmt.execute();
            conn.close();
        } catch (SQLException e) {
            Bukkit.getServer().getLogger().warning("Could not delete table homes.");
        }
        String sqlCreateHomeTable = "CREATE TABLE homes ("
                + "homeID SERIAL NOT NULL PRIMARY KEY,"
                + "uuid TEXT NOT NULL,"
                + "homename TEXT NOT NULL,"
                + "x DOUBLE PRECISION NOT NULL,"
                + "y DOUBLE PRECISION NOT NULL,"
                + "z DOUBLE PRECISION NOT NULL,"
                + "world TEXT NOT NULL"
                + ")"
                ;

        Connection connection = getConnection();

        PreparedStatement createHomeTableStatement = connection.prepareStatement(sqlCreateHomeTable);
        createHomeTableStatement.execute();
        connection.close();
    }
    public List<PlayerHome> getAllPlayerHomes(@NonNull Player player) throws SQLException {
        String uuid = player.getUniqueId().toString();
        String sql = "SELECT * FROM homes WHERE uuid = '" + uuid + "'";
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
       String sql = "INSERT INTO homes (uuid, homename, x, y, z, world) VALUES ("
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
        String sql = "DELETE FROM homes WHERE "
                + "uuid = '" + home.getUuid() + "' AND "
                + "homename = '" + home.getHomeName() + "'";
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.execute();
        connection.close();
    }
}
