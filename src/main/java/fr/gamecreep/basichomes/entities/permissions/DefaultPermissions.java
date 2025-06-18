package fr.gamecreep.basichomes.entities.permissions;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class DefaultPermissions implements PermissionEntry {
    @NonNull
    private GroupPermission group;
    @NonNull
    private final Map<String, Boolean> permissions = new HashMap<>();

    @Getter
    @RequiredArgsConstructor
    public enum GroupPermission {
        ALL("all"),
        OP("op");

        private final String valueString;
    }
}
