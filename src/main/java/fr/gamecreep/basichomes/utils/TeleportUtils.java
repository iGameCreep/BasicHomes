package fr.gamecreep.basichomes.utils;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.Constants;
import fr.gamecreep.basichomes.config.enums.ConfigElement;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeleportUtils {
    private final BasicHomes plugin;
    private final Map<UUID, SavedPosition> tpQueue = new HashMap<>();
    private final Map<UUID, Integer> internalQueue = new HashMap<>();

    private int delay;
    private boolean standStill;

    public TeleportUtils(@NonNull final BasicHomes plugin) {
        this.plugin = plugin;
        resetConfig(null);
    }

    private void resetConfig(final PositionType type) {
        if (type == PositionType.HOME) {
            this.delay = (int) plugin.getPluginConfig().getConfig().get(ConfigElement.HOMES_DELAY);
            this.standStill = (boolean) plugin.getPluginConfig().getConfig().get(ConfigElement.HOMES_STAND_STILL);
        } else if (type == PositionType.WARP) {
            this.delay = (int) plugin.getPluginConfig().getConfig().get(ConfigElement.WARPS_DELAY);
            this.standStill = (boolean) plugin.getPluginConfig().getConfig().get(ConfigElement.WARPS_STAND_STILL);
        }
    }

    public void add(@NonNull final Player player, @NonNull final SavedPosition destination) {
        final PositionType type = destination.getType();
        if (type == null) {
            ChatUtils.sendPlayerError(player, "Internal error: could not retrieve the position type. Please try again later or contact an administrator/developer.");
            return;
        }

        resetConfig(type);

        if (this.delay == 0) {
            teleportPlayer(player, destination);
            return;
        }

        if (isQueued(player)) {
            ChatUtils.sendPlayerError(player, "You are already teleporting somewhere !");
            return;
        }

        this.tpQueue.put(player.getUniqueId(), destination);

        this.prepareTeleport(player, this.delay);

        String message = String.format("Teleporting you to %s%s%s in %s%s%s seconds...",
                Constants.SPECIAL_COLOR,
                destination.getName(),
                Constants.SUCCESS_COLOR,
                Constants.SPECIAL_COLOR,
                this.delay,
                Constants.SUCCESS_COLOR);

        if (this.standStill) {
            message += " Stand still !";
        }

        ChatUtils.sendPlayerInfo(player, message);
    }

    public boolean isQueued(@NonNull final Player player) {
        return this.tpQueue.containsKey(player.getUniqueId());
    }

    public void playerMoved(@NonNull final Player player) {
        final SavedPosition pos = this.tpQueue.get(player.getUniqueId());
        if (pos.getType() == null) return;

        resetConfig(pos.getType());

        if (!isQueued(player)) return;

        if (this.standStill) {
            ChatUtils.sendPlayerError(player, "Teleport has been canceled because you moved.");

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

    private void teleportPlayer(@NonNull final Player player, @NonNull final SavedPosition pos) {
        Location loc = pos.getLocation();
        loc.setPitch(player.getLocation().getPitch());
        loc.setYaw(player.getLocation().getYaw());

        player.teleport(loc);

        this.tpQueue.remove(player.getUniqueId());
        this.internalQueue.remove(player.getUniqueId());

        ChatUtils.sendPlayerInfo(player, String.format("Successfully teleported you to %s%s%s !",
                Constants.SPECIAL_COLOR,
                pos.getName(),
                Constants.SUCCESS_COLOR
        ));
    }
}
