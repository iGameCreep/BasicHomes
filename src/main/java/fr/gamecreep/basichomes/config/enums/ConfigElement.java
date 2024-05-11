package fr.gamecreep.basichomes.config.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfigElement {
    HOMES_ENABLED("homes.enabled", true),
    HOMES_DELAY("homes.delay", 0),
    HOMES_STAND_STILL("homes.standStill", true),

    WARPS_ENABLED("warps.enabled", true),
    WARPS_DELAY("warps.delay", 0),
    WARPS_STAND_STILL("warps.standStill", true),

    OP_BYPASS_HOME_LIMIT("op-bypass-home-limit", false),
    MAX_HOMES("max-homes", 3);

    private final String path;
    private final Object defaultValue;

    public Object parseValue(String value) {
        if (defaultValue instanceof Integer) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (defaultValue instanceof Boolean) {
            return Boolean.parseBoolean(value);
        }
        return value;
    }
}
