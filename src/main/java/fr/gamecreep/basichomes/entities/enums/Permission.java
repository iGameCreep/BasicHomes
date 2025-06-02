package fr.gamecreep.basichomes.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Permission {
    CONFIG("basichomes.config"),
    PERMISSIONS("basichomes.permissions"),

    CREATE_WARP("basichomes.warp.create"),
    DELETE_WARP("basichomes.warp.delete"),
    UPDATE_WARP("basichomes.warp.update"),
    USE_WARP("basichomes.warp.use"),

    USE_HOME("basichomes.home.use"),
    MANAGE_HOME("basichomes.home.manage");

    private final String name;

    public static List<String> getAll() {
        return Arrays.stream(Permission.values())
                .map(Permission::getName)
                .toList();
    }
}
