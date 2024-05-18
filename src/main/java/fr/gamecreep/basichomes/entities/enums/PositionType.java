package fr.gamecreep.basichomes.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PositionType {
    HOME(
            "home"
    ),
    WARP(
            "warp"
    );

    private final String displayName;
}
