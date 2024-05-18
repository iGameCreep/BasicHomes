package fr.gamecreep.basichomes.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfigElement {
    HOMES_ENABLED("homes.enabled", DataType.BOOLEAN, true),
    HOMES_DELAY("homes.delay", DataType.INTEGER, 0),
    HOMES_STAND_STILL("homes.standStill", DataType.BOOLEAN, true),

    WARPS_ENABLED("warps.enabled", DataType.BOOLEAN, true),
    WARPS_DELAY("warps.delay", DataType.INTEGER, 0),
    WARPS_STAND_STILL("warps.standStill", DataType.BOOLEAN, true),

    OP_BYPASS_HOME_LIMIT("op-bypass-home-limit", DataType.BOOLEAN, false),
    MAX_HOMES("max-homes", DataType.INTEGER, 3);

    private final String path;
    private final DataType type;
    private final Object defaultValue;

    public Object parseValue(final String value) {
        if (type == DataType.INTEGER) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (type == DataType.BOOLEAN) {
            return Boolean.parseBoolean(value);
        }
        return value;
    }
}
