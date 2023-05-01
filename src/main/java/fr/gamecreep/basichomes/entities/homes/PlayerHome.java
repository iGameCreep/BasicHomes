package fr.gamecreep.basichomes.entities.homes;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Data
public class PlayerHome {
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private String world;
    private String homeName;
    private String uuid;

    public PlayerHome(@NonNull String name, String uuid, @NonNull Location loc) {
        setX(loc.getX());
        setY(loc.getY());
        setZ(loc.getZ());
        setPitch(loc.getPitch());
        setYaw(loc.getYaw());
        setWorld(loc.getWorld().getName());
        setHomeName(name);
        setUuid(uuid);
    }

    public PlayerHome(@NonNull String name, @NonNull Player player, @NonNull Location loc) {
        setX(loc.getX());
        setY(loc.getY());
        setZ(loc.getZ());
        setPitch(loc.getPitch());
        setYaw(loc.getYaw());
        setWorld(loc.getWorld().getName());
        setHomeName(name);
        setUuid(player.getUniqueId().toString());
    }

    public Location getLocation() {
        World homeWorld = Bukkit.getWorld(this.world);
        Location location = new Location(homeWorld, this.x, this.y, this.z);
        return location;
    }
}
