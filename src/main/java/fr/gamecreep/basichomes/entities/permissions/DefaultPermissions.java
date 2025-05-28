package fr.gamecreep.basichomes.entities.permissions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

@Getter
public class DefaultPermissions extends PermissionEntry {
    private final GroupPermission groupPermission;

    public DefaultPermissions(@NonNull final UUID id,
                             @NonNull final List<String> permissionList,
                             @NonNull final GroupPermission groupPermission) {
        super(id, permissionList);
        this.groupPermission = groupPermission;
    }

    @AllArgsConstructor
    @Getter
    public enum GroupPermission {
        ALL("all"),
        OP("op");

        private final String stringValue;
    }
}
