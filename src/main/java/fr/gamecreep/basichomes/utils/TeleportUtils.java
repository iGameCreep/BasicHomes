package fr.gamecreep.basichomes.utils;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.config.enums.ConfigElement;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class TeleportUtils {
    private final BasicHomes plugin;
    private final Map<UUID, SavedPosition> tpQueue = new HashMap<>();
    private final Map<UUID, Integer> internalQueue = new HashMap<>();
    private int homeDelay;
    private int warpDelay;
    private boolean homeDelayEnabled;
    private boolean warpDelayEnabled;
    private boolean homeStandStill;
    private boolean warpStandStill;

    public TeleportUtils(@NonNull final BasicHomes plugin) {
        this.plugin = plugin;
        this.homeDelay = (int) plugin.getPluginConfig().getConfig().get(ConfigElement.HOMES_DELAY);
        this.warpDelay = (int) plugin.getPluginConfig().getConfig().get(ConfigElement.WARPS_DELAY);
        this.homeDelayEnabled = (this.homeDelay != 0);
        this.warpDelayEnabled = (this.warpDelay != 0);
        this.homeStandStill = (boolean) plugin.getPluginConfig().getConfig().get(ConfigElement.HOMES_STAND_STILL);
        this.warpStandStill = (boolean) plugin.getPluginConfig().getConfig().get(ConfigElement.WARPS_STAND_STILL);
    }

    private void resetConfig() {
        this.homeDelay = (int) plugin.getPluginConfig().getConfig().get(ConfigElement.HOMES_DELAY);
        this.warpDelay = (int) plugin.getPluginConfig().getConfig().get(ConfigElement.WARPS_DELAY);
        this.homeDelayEnabled = (this.homeDelay != 0);
        this.warpDelayEnabled = (this.warpDelay != 0);
        this.homeStandStill = (boolean) plugin.getPluginConfig().getConfig().get(ConfigElement.HOMES_STAND_STILL);
        this.warpStandStill = (boolean) plugin.getPluginConfig().getConfig().get(ConfigElement.WARPS_STAND_STILL);
    }

    public void add(@NonNull final Player player, @NonNull final SavedPosition destination) {
        resetConfig();

        final PositionType type = destination.getType();
        if (type == null) {
            this.plugin.getChatUtils().sendPlayerError(player, "Internal error: could not retrieve the position type. Please try again later or contact an administrator/developer.");
            return;
        }

        final boolean delayTeleport = type == PositionType.HOME ? this.homeDelayEnabled : this.warpDelayEnabled;
        if (!delayTeleport) {
            teleportPlayer(player, destination);
            return;
        }

        if (isQueued(player)) {
            this.plugin.getChatUtils().sendPlayerError(player, "You are already teleporting somewhere !");
            return;
        }

        this.tpQueue.put(player.getUniqueId(), destination);

        final int delay = type == PositionType.HOME ? this.homeDelay : warpDelay;
        this.prepareTeleport(player, delay);

        String message = String.format("Teleporting you to %s%s%s in %s%s%s seconds...",
                Constants.SPECIAL_COLOR,
                destination.getName(),
                Constants.SUCCESS_COLOR,
                Constants.SPECIAL_COLOR,
                delay,
                Constants.SUCCESS_COLOR);

        final boolean standStill = type == PositionType.HOME ? this.homeStandStill : this.warpStandStill;
        if (standStill) {
            message += " Stand still !";
        }

        this.plugin.getChatUtils().sendPlayerInfo(player, message);
    }

    public boolean isQueued(@NonNull final Player player) {
        return this.tpQueue.containsKey(player.getUniqueId());
    }

    public void playerMoved(@NonNull final Player player) {
        resetConfig();

        if (!isQueued(player)) return;

        final SavedPosition pos = this.tpQueue.get(player.getUniqueId());
        if (pos.getType() == null) return;

        final boolean needToStandStill = pos.getType() == PositionType.HOME ? this.homeStandStill : this.warpStandStill;

        if (needToStandStill) {
            this.plugin.getChatUtils().sendPlayerError(player, "Teleport has been canceled because you moved.");
            this.tpQueue.remove(player.getUniqueId());
            Bukkit.getScheduler().cancelTask(this.internalQueue.get(player.getUniqueId()));
            this.internalQueue.remove(player.getUniqueId());
        }
    }

    private void prepareTeleport(@NonNull final Player player, final int delay) {
        final BukkitTask task = Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            final SavedPosition pos = this.tpQueue.get(player.getUniqueId());
            if (pos == null) return;

            this.teleportPlayer(player, pos);
        }, (delay * 20L));

        this.internalQueue.put(player.getUniqueId(), task.getTaskId());
    }

    private void teleportPlayer(@NonNull final Player player, @NonNull SavedPosition pos) {
        Location loc = pos.getLocation();
        loc.setPitch(player.getLocation().getPitch());
        loc.setYaw(player.getLocation().getYaw());
        player.teleport(loc);
        this.tpQueue.remove(player.getUniqueId());
        this.internalQueue.remove(player.getUniqueId());

        this.plugin.getChatUtils().sendPlayerInfo(player, String.format("Successfully teleported you to %s%s%s !",
                Constants.SPECIAL_COLOR,
                pos.getName(),
                Constants.SUCCESS_COLOR
        ));
    }
}
