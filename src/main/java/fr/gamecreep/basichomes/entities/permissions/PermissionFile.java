package fr.gamecreep.basichomes.entities.permissions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;

@AllArgsConstructor
@Getter
public class PermissionFile {
    private final PermissionEntryList<PlayerPermissions> playerPermissions;
    private final PermissionEntryList<DefaultPermissions> defaultPermissions;

    public PermissionFile() {
        this.playerPermissions = new PermissionEntryList<>(Collections.emptyList());
        this.defaultPermissions = new PermissionEntryList<>(Collections.emptyList());
    }
}
