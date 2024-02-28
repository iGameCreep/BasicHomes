package fr.gamecreep.basichomes.entities.enums;

import fr.gamecreep.basichomes.Constants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MenuType {
    HOME(PositionType.HOME, Constants.SPECIAL_COLOR + "My Homes"),
    HOME_OF(PositionType.HOME, Constants.HOMES_OF_START_MENU_NAME),
    WARP(PositionType.WARP, Constants.SPECIAL_COLOR + "Warps");

    private final PositionType type;
    private final String startOfMenuName;
}
