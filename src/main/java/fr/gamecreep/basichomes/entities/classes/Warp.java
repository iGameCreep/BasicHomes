package fr.gamecreep.basichomes.entities.classes;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

@Getter
@Setter
public class Warp extends Default {
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private String world;

    public Warp(@NonNull String name, @NonNull String ownerUuid, @NonNull Location loc) {
        setX(loc.getX());
        setY(loc.getY());
        setZ(loc.getZ());
        setPitch(loc.getPitch());
        setYaw(loc.getYaw());
        setWorld(Objects.requireNonNull(loc.getWorld()).getName());
        setName(name);
        setOwnerUuid(ownerUuid);
    }

    public Location getLocation() {
        World w = Bukkit.getWorld(this.world);
        return new Location(w, this.x, this.y, this.z);
    }
}
