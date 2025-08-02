package fr.gamecreep.basichomes.entities.permissions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class PermissionFile {
    private final List<PlayerPermissions> playerPermissions;
    private final List<DefaultPermissions> defaultPermissions;

    public PermissionFile() {
        this.playerPermissions = new ArrayList<>();
        this.defaultPermissions = new ArrayList<>();
    }
}
