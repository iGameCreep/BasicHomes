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
        Location from = event.getFrom();
        Location to = event.getTo();
        Player player = event.getPlayer();
        boolean isQueued = this.teleportUtils.isQueued(player);

        if (to == null) return;
        if (isQueued && hasBlockChanged(from, to)) {
            this.teleportUtils.playerMoved(player);
        }
    }

    private boolean hasBlockChanged(Location a, Location b) {
        return a.getX() != b.getX() || a.getY() != b.getY() || a.getZ() != b.getZ();
    }
}
