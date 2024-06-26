package fr.gamecreep.basichomes.events;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.utils.TeleportUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TeleportEvents implements Listener {
    private final TeleportUtils teleportUtils;

    public TeleportEvents(BasicHomes plugin) {
        this.teleportUtils = plugin.getTeleportUtils();
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        final Location from = event.getFrom();
        final Location to = event.getTo();
        final Player player = event.getPlayer();
        final boolean isQueued = this.teleportUtils.isQueued(player);

        if (to == null) return;
        if (isQueued && hasBlockChanged(from, to)) {
            this.teleportUtils.playerMoved(player);
        }
    }

    private boolean hasBlockChanged(final Location a, final Location b) {
        return Math.round(a.getX()) != Math.round(b.getX())
                || Math.round(a.getY()) != Math.round(b.getY())
                || Math.round(a.getZ()) != Math.round(b.getZ());
    }
}
