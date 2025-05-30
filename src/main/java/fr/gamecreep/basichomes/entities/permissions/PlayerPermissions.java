package fr.gamecreep.basichomes.entities.permissions;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PlayerPermissions implements PermissionEntry {
    @NonNull
    private UUID playerId;
    @NonNull
    private final Map<String, Boolean> permissions = new HashMap<>();
}
