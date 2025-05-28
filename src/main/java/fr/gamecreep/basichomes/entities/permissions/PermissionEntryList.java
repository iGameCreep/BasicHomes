package fr.gamecreep.basichomes.entities.permissions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PermissionEntryList<T extends PermissionEntry> {
    private final List<T> permissionEntries;
}
