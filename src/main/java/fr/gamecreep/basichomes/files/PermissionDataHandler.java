package fr.gamecreep.basichomes.files;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.permissions.PermissionFile;
import lombok.NonNull;

public class PermissionDataHandler {

    private final BasicHomes plugin;
    private final DataStore<PermissionFile> permissionDataStore;

    public PermissionDataHandler(@NonNull final BasicHomes plugin) {
        this.plugin = plugin;
        this.permissionDataStore = new DataStore<>(plugin, "permissions.json", new PermissionFile());
    }

    // TODO : methods to add/remove permissions for both player and default (all/op)
}
