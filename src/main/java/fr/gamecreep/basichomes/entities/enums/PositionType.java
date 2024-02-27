package fr.gamecreep.basichomes.entities.enums;

import fr.gamecreep.basichomes.Constants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PositionType {
    HOME(
            "home",
            Constants.SPECIAL_COLOR + "My Homes",
            Constants.WARNING_COLOR + "Delete this home"
    ),
    WARP(
            "warp",
            Constants.SPECIAL_COLOR + "Warps",
            Constants.WARNING_COLOR + "Delete this warp"
    );

    private final String displayName;
    private final String menuName;
    private final String deleteText;
}
