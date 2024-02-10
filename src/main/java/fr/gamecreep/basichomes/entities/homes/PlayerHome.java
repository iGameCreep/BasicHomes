package fr.gamecreep.basichomes.entities.homes;

import lombok.Data;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

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

    public PlayerHome(@NonNull String name, @NonNull String playerUuid, @NonNull Location loc) {
        setX(loc.getX());
        setY(loc.getY());
        setZ(loc.getZ());
        setPitch(loc.getPitch());
        setYaw(loc.getYaw());
        setWorld(Objects.requireNonNull(loc.getWorld()).getName());
        setHomeName(name);
        setUuid(playerUuid);
    }

    public Location getLocation() {
        World homeWorld = Bukkit.getWorld(this.world);
        return new Location(homeWorld, this.x, this.y, this.z);
    }
}
