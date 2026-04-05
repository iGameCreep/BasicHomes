package fr.gamecreep.basichomes;

import dev.faststats.bukkit.BukkitMetrics;
import dev.faststats.core.ErrorTracker;
import dev.faststats.core.chart.Chart;
import fr.gamecreep.basichomes.config.enums.ConfigElement;
import org.bstats.charts.SimplePie;

import java.util.Map;

public class MetricsLoader {
    public static final ErrorTracker ERROR_TRACKER = ErrorTracker.contextAware();

    private final BasicHomes plugin;

    public MetricsLoader(BasicHomes plugin) {
        this.plugin = plugin;
    }

    public void loadbStats() {
        org.bstats.bukkit.Metrics metrics = new org.bstats.bukkit.Metrics(this.plugin, Constants.BSTATS_PLUGIN_ID);

        SimplePie warpsChart = new SimplePie("using_warps", () -> Boolean.toString(getWarpMetricValue()));
        metrics.addCustomChart(warpsChart);
    }

    public void loadFastStats() {
        Chart<Boolean> warpsChart = Chart.bool("using_warps", this::getWarpMetricValue);

        BukkitMetrics.factory()
                .token(Constants.FASTSTATS_TOKEN)
                .errorTracker(ERROR_TRACKER)
                .addChart(warpsChart)
                .create(plugin);
    }

    private boolean getWarpMetricValue() {
        Map<ConfigElement, Object> pluginConfig = plugin.getPluginConfig().getConfig();

        return (Boolean) pluginConfig.getOrDefault(ConfigElement.WARPS_ENABLED, false);
    }
}
