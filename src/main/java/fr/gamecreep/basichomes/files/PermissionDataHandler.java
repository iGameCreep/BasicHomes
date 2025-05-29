package fr.gamecreep.basichomes.files;

import com.google.gson.reflect.TypeToken;
import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.permissions.DefaultPermissions;
import fr.gamecreep.basichomes.entities.permissions.PermissionFile;
import fr.gamecreep.basichomes.entities.permissions.PlayerPermissions;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.List;
import java.util.UUID;

public class PermissionDataHandler {

    private final BasicHomes plugin;
    private final DataStore<PermissionFile> dataStore;
    private final PermissionFile permissionFile;

    public PermissionDataHandler(@NonNull final BasicHomes plugin) {
        this.plugin = plugin;
        this.dataStore = new DataStore<>("permissions.json", new PermissionFile(), new TypeToken<PermissionFile>(){}.getType());
        this.permissionFile = dataStore.getData();
    }

    public void setPermission(@NonNull final UUID playerId, @NonNull final String permissionNode, final boolean value) {
        final PlayerPermissions entry = getOrCreatePlayerEntry(playerId);
        entry.getPermissions().put(permissionNode, value);
        this.dataStore.save();
    }

    public void setDefaultPermission(@NonNull final DefaultPermissions.GroupPermission group,
                                     @NonNull final String permissionNode,
                                     final boolean value) {
        final DefaultPermissions entry = getOrCreateDefaultEntry(group);
        entry.getPermissions().put(permissionNode, value);
        this.dataStore.save();
    }

    public void removePermission(@NonNull final UUID playerId, @NonNull final String permissionNode) {
        final PlayerPermissions entry = getOrCreatePlayerEntry(playerId);
        entry.getPermissions().remove(permissionNode);
        this.dataStore.save();
    }

    public void removeDefaultPermission(@NonNull final DefaultPermissions.GroupPermission group,
                                        @NonNull final String permissionNode) {
        final DefaultPermissions entry = getOrCreateDefaultEntry(group);
        entry.getPermissions().remove(permissionNode);
        this.dataStore.save();
    }

    private PlayerPermissions getOrCreatePlayerEntry(@NonNull final UUID playerId) {
        return this.permissionFile.getPlayerPermissions().stream()
                .filter(e -> e.getPlayerId().equals(playerId))
                .findFirst()
                .orElseGet(() -> {
                    final PlayerPermissions newEntry = new PlayerPermissions(playerId);
                    this.permissionFile.getPlayerPermissions().add(newEntry);
                    return newEntry;
                });
    }

    private DefaultPermissions getOrCreateDefaultEntry(@NonNull final DefaultPermissions.GroupPermission group) {
        return this.permissionFile.getDefaultPermissions().stream()
                .filter(e -> e.getGroup() == group)
                .findFirst()
                .orElseGet(() -> {
                    final DefaultPermissions newEntry = new DefaultPermissions(group);
                    this.permissionFile.getDefaultPermissions().add(newEntry);
                    return newEntry;
                });
    }

    public List<PlayerPermissions> getPlayerPermissions() {
        return this.permissionFile.getPlayerPermissions();
    }

    public List<DefaultPermissions> getDefaultPermissions() {
        return this.permissionFile.getDefaultPermissions();
    }

    public void applyPermissions(@NonNull final Player player) {
        final UUID playerId = player.getUniqueId();

        final PermissionAttachment permissionAttachment =
                this.plugin.getPermissionAttachments().getOrDefault(playerId, player.addAttachment(this.plugin));

        permissionAttachment.getPermissions().clear();

        this.getPlayerPermissions().stream()
                .filter(p -> p.getPlayerId().equals(playerId))
                .findFirst()
                .ifPresent(entry -> entry.getPermissions().forEach(permissionAttachment::setPermission));

        final DefaultPermissions.GroupPermission group = player.isOp()
                ? DefaultPermissions.GroupPermission.OP
                : DefaultPermissions.GroupPermission.ALL;

        this.getDefaultPermissions().stream()
                .filter(d -> d.getGroup().equals(group))
                .findFirst()
                .ifPresent(entry -> entry.getPermissions().forEach(permissionAttachment::setPermission));

        this.plugin.getPermissionAttachments().put(playerId, permissionAttachment);
    }
}
