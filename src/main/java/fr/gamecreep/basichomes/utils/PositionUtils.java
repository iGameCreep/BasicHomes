package fr.gamecreep.basichomes.utils;

import fr.gamecreep.basichomes.Constants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.World;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PositionUtils {
    public static Material getMaterialFromWorldEnvironment(final World.Environment environment) {
        return switch (environment) {
            case NORMAL -> Constants.HOME_OVERWORLD_ITEM;
            case NETHER -> Constants.HOME_NETHER_ITEM;
            case THE_END -> Constants.HOME_END_ITEM;
            default -> Constants.DEFAULT_HOME_ITEM;
        };
    }
}
