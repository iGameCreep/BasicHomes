package fr.gamecreep.basichomes.entities.permissions;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public abstract class PermissionEntry {
    private final UUID id;
    final List<String> permissionList;
}
