package fr.gamecreep.basichomes.entities.permissions;

import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

@Getter
public class PlayerPermissions extends PermissionEntry {
    private final UUID playerId;

    public PlayerPermissions(@NonNull final UUID id,
                            @NonNull final List<String> permissionList,
                            @NonNull final UUID playerId) {
        super(id, permissionList);
        this.playerId = playerId;
    }
}
