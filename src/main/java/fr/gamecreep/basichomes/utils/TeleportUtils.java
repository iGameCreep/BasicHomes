package fr.gamecreep.basichomes.utils;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.classes.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

public class TeleportUtils {
    private final BasicHomes plugin;
    private final Map<UUID, SavedPosition> tpQueue = new HashMap<>();
    private final boolean homeDelayEnabled;
    private final boolean warpDelayEnabled;
    private final int homeDelay;
    private final int warpDelay;
    private final boolean homeStandStill;
    private final boolean warpStandStill;

    public TeleportUtils(@NonNull final BasicHomes plugin) {
        this.plugin = plugin;
        this.homeDelayEnabled = plugin.getPluginConfig().getHomesConfig().isDelayTeleport();
        this.warpDelayEnabled = plugin.getPluginConfig().getWarpsConfig().isDelayTeleport();
        this.homeDelay = plugin.getPluginConfig().getHomesConfig().getDelay();
        this.warpDelay = plugin.getPluginConfig().getWarpsConfig().getDelay();
        this.homeStandStill = plugin.getPluginConfig().getHomesConfig().isStandStill();
        this.warpStandStill = plugin.getPluginConfig().getWarpsConfig().isStandStill();
    }

    public boolean add(@NonNull final Player player, @NonNull final SavedPosition destination, @NonNull final PositionType type) {
        boolean delayTeleport = type == PositionType.HOME ? this.homeDelayEnabled : this.warpDelayEnabled;
        if (!delayTeleport) {
            teleportPlayer(player, destination);
            return true;
        }
        if (destination.getType() == null) destination.setType(type);
        if (isQueued(player)) {
            this.plugin.getChatUtils().sendPlayerError(player, "You are already teleporting somewhere !");
            return false;
        }

        this.tpQueue.put(player.getUniqueId(), destination);
        int delay = type == PositionType.HOME ? this.homeDelay : warpDelay;
        this.prepareTeleport(player, delay);

        String message = String.format("Teleporting you to %s%s%s in %s%s%s seconds...",
                Constants.SPECIAL_COLOR,
                destination.getName(),
                Constants.SUCCESS_COLOR,
                Constants.SPECIAL_COLOR,
                delay,
                Constants.SUCCESS_COLOR);

        boolean standStill = type == PositionType.HOME ? this.homeStandStill : this.warpStandStill;
        if (standStill) {
            message += " Stand still !";
        }

        this.plugin.getChatUtils().sendPlayerInfo(player, message);

        return true;
    }

    public boolean isQueued(@NonNull final Player player) {
        return this.tpQueue.containsKey(player.getUniqueId());
    }

    public void playerMoved(@NonNull final Player player) {
        if (!isQueued(player)) return;
        SavedPosition pos = this.tpQueue.get(player.getUniqueId());

        if (pos.getType() == null) return;
        boolean needToStandStill = pos.getType() == PositionType.HOME ? this.homeStandStill : this.warpStandStill;

        if (needToStandStill) {
            this.plugin.getChatUtils().sendPlayerError(player, "Teleport has been canceled because you were moving.");
            this.tpQueue.remove(player.getUniqueId());
        }
    }

    private void prepareTeleport(@NonNull final Player player, int delay) {
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            SavedPosition pos = this.tpQueue.get(player.getUniqueId());
            if (pos == null) return;

            this.teleportPlayer(player, pos);

        }, (delay * 20L));
    }

    private void teleportPlayer(@NonNull final Player player, @NonNull SavedPosition pos) {
        Location loc = pos.getLocation();
        loc.setPitch(player.getLocation().getPitch());
        loc.setYaw(player.getLocation().getYaw());
        player.teleport(loc);
        this.tpQueue.remove(player.getUniqueId());

        this.plugin.getChatUtils().sendPlayerInfo(player, String.format("Successfully teleported you to %s%s%s !",
                Constants.SPECIAL_COLOR,
                pos.getName(),
                Constants.SUCCESS_COLOR
        ));
    }
}
