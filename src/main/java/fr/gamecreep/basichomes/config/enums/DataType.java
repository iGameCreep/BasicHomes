package fr.gamecreep.basichomes.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DataType {
    BOOLEAN("boolean"),
    INTEGER("integer");

    private final String type;
}
