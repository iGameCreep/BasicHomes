package fr.gamecreep.basichomes.entities.enums;

import fr.gamecreep.basichomes.Constants;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PositionType {
    HOME(
            "home",
            Constants.WARNING_COLOR + "Delete this home"
    ),
    WARP(
            "warp",
            Constants.WARNING_COLOR + "Delete this warp"
    );

    private final String displayName;
    private final String deleteText;
}
