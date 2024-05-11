package fr.gamecreep.basichomes.config;

import fr.gamecreep.basichomes.config.enums.ConfigElement;
import lombok.Getter;
import lombok.Setter;

import java.util.EnumMap;
import java.util.Map;

@Getter
@Setter
public class PluginConfig {
    private Map<ConfigElement, Object> config = new EnumMap<>(ConfigElement.class);
}
