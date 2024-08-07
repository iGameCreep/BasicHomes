package fr.gamecreep.basichomes.entities;

import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.utils.PositionUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class SavedPosition {
    private UUID id;
    private String name;
    private String ownerUuid;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private String world;
    private PositionType type;
    private Material block;

    public SavedPosition(final String name, final String ownerUuid, @NonNull final Material block, @NonNull final Location loc, @NonNull final PositionType type) {
        setId(UUID.randomUUID());
        setX(loc.getX());
        setY(loc.getY());
        setZ(loc.getZ());
        setPitch(loc.getPitch());
        setYaw(loc.getYaw());
        setWorld(Objects.requireNonNull(loc.getWorld()).getName());
        setName(name);
        setOwnerUuid(ownerUuid);
        setType(type);
        setBlock(block);
    }

    public SavedPosition(final String name, final String ownerUuid, @NonNull final Location loc, @NonNull final PositionType type) {
        setId(UUID.randomUUID());
        setX(loc.getX());
        setY(loc.getY());
        setZ(loc.getZ());
        setPitch(loc.getPitch());
        setYaw(loc.getYaw());
        setWorld(Objects.requireNonNull(loc.getWorld()).getName());
        setName(name);
        setOwnerUuid(ownerUuid);
        setType(type);
        setBlock(PositionUtils.getMaterialFromWorldEnvironment(loc.getWorld().getEnvironment()));
    }

    public Location getLocation() {
        final World w = Bukkit.getWorld(this.world);
        return new Location(w, this.x, this.y, this.z);
    }
}
