package fr.gamecreep.basichomes.files;

import com.google.gson.reflect.TypeToken;
import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.permissions.DefaultPermissions;
import fr.gamecreep.basichomes.entities.permissions.PermissionFile;
import fr.gamecreep.basichomes.entities.permissions.PlayerPermissions;
import fr.gamecreep.basichomes.utils.LoggerUtils;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import java.util.*;

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

        Player player = this.plugin.getServer().getPlayer(playerId);
        if (player != null) {
            this.refreshPermissions(player);
        }
    }

    public void setDefaultPermission(@NonNull final DefaultPermissions.GroupPermission group,
                                     @NonNull final String permissionNode,
                                     final boolean value) {
        final DefaultPermissions entry = getOrCreateDefaultEntry(group);
        entry.getPermissions().put(permissionNode, value);
        this.dataStore.save();
        this.applyDefaultPermissions();
    }

    public void removePermission(@NonNull final UUID playerId, @NonNull final String permissionNode) {
        final PlayerPermissions entry = getOrCreatePlayerEntry(playerId);
        entry.getPermissions().remove(permissionNode);
        this.dataStore.save();

        Player player = this.plugin.getServer().getPlayer(playerId);
        if (player != null) {
            this.refreshPermissions(player);
        }
    }

    public void removeDefaultPermission(@NonNull final DefaultPermissions.GroupPermission group,
                                        @NonNull final String permissionNode) {
        final DefaultPermissions entry = getOrCreateDefaultEntry(group);
        entry.getPermissions().remove(permissionNode);
        this.dataStore.save();
        this.applyDefaultPermissions();
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

    public List<PlayerPermissions> getAllPlayerPermissions() {
        return this.permissionFile.getPlayerPermissions();
    }

    public Map<String, Boolean> getPlayerPermissions(@NonNull final UUID playerId) {
        for (final PlayerPermissions playerPermissions : this.getAllPlayerPermissions()) {
            if (playerPermissions.getPlayerId().equals(playerId)) {
                return playerPermissions.getPermissions();
            }
        }
        return Collections.emptyMap();
    }

    public List<String> getPlayerEnabledPermissions(@NonNull final UUID playerId) {
        final List<String> permissions = new ArrayList<>();
        final Map<String, Boolean> playerPermissions = this.getPlayerPermissions(playerId);

        for (final Map.Entry<String, Boolean> permEntry : playerPermissions.entrySet()) {
            if (Boolean.TRUE.equals(permEntry.getValue())) {
                permissions.add(permEntry.getKey());
            }
        }

        return permissions;
    }

    public List<DefaultPermissions> getAllDefaultPermissions() {
        return this.permissionFile.getDefaultPermissions();
    }

    public Map<String, Boolean> getDefaultPermissions(@NonNull final DefaultPermissions.GroupPermission group) {
        for (final DefaultPermissions defaultPermissions : this.getAllDefaultPermissions()) {
            if (defaultPermissions.getGroup().equals(group)) {
                return defaultPermissions.getPermissions();
            }
        }
        return Collections.emptyMap();
    }

    public List<String> getDefaultEnabledPermissions(@NonNull final DefaultPermissions.GroupPermission group) {
        final List<String> permissions = new ArrayList<>();
        final Map<String, Boolean> groupPermissions = this.getDefaultPermissions(group);

        for (final Map.Entry<String, Boolean> permEntry : groupPermissions.entrySet()) {
            if (Boolean.TRUE.equals(permEntry.getValue())) {
                permissions.add(permEntry.getKey());
            }
        }

        return permissions;
    }

    public List<DefaultPermissions.GroupPermission> getPlayerGroups(@NonNull final Player player) {
        final List<DefaultPermissions.GroupPermission> groups = new ArrayList<>();
        groups.add(DefaultPermissions.GroupPermission.ALL);
        if (player.isOp()) {
            groups.add(DefaultPermissions.GroupPermission.OP);
        }

        return groups;
    }

    private void applyDefaultPermissions() {
        for (final Player player : this.plugin.getServer().getOnlinePlayers()) {
            this.refreshPermissions(player);
        }
    }

    public void refreshPermissions(@NonNull Player player) {
        UUID uuid = player.getUniqueId();
        PermissionAttachment attachment = this.plugin.getPermissionAttachments().get(uuid);

        if (attachment == null) {
            // Fallback: create a new attachment
            attachment = player.addAttachment(this.plugin);
            this.plugin.getPermissionAttachments().put(uuid, attachment);
            LoggerUtils.logInfo("Recreating permission attachment for player " + uuid);
        }

        for (String key : new HashSet<>(attachment.getPermissions().keySet())) {
            attachment.unsetPermission(key);
        }

        for (DefaultPermissions.GroupPermission group : getPlayerGroups(player)) {
            getDefaultPermissions(group).forEach(attachment::setPermission);
        }

        getPlayerPermissions(uuid).forEach(attachment::setPermission);
    }

    public void handlePlayerJoin(Player player) {
        PermissionAttachment attachment = player.addAttachment(this.plugin);
        this.plugin.getPermissionAttachments().put(player.getUniqueId(), attachment);

        this.refreshPermissions(player);
    }

    public void handlePlayerQuit(Player player) {
        UUID uuid = player.getUniqueId();
        PermissionAttachment att = this.plugin.getPermissionAttachments().remove(uuid);

        if (att != null) {
            try {
                player.removeAttachment(att);
            } catch (IllegalArgumentException ignored) {
                LoggerUtils.logWarning("Unable to remove permission attachment for player " + uuid);
            }
        }
    }

}
