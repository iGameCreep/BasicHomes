package fr.gamecreep.basichomes.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    CREATE_WARP("basichomes.warp.create"),
    DELETE_WARP("basichomes.warp.delete"),
    USE_WARP("basichomes.warp.use"),

    USE_HOME("basichomes.home.use"),
    MANAGE_HOME("basichomes.home.manage");

    private final String name;
}
