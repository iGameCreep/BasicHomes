package fr.gamecreep.basichomes.events;

import fr.gamecreep.basichomes.BasicHomes;
import lombok.NonNull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PermissionEvents implements Listener {

    private final BasicHomes plugin;

    public PermissionEvents(BasicHomes plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onPlayerJoin(@NonNull final PlayerJoinEvent event) {
        this.plugin.getPermissionDataHandler().applyPermissions(event.getPlayer());
    }
}
