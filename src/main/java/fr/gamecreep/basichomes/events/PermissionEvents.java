package fr.gamecreep.basichomes.events;

import fr.gamecreep.basichomes.BasicHomes;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PermissionEvents implements Listener {

    private final BasicHomes plugin;

    public PermissionEvents(BasicHomes plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerJoin(PlayerJoinEvent event) {
        this.plugin.getPermissionDataHandler().handlePlayerJoin(event.getPlayer());
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        this.plugin.getPermissionDataHandler().handlePlayerQuit(event.getPlayer());
    }
}
