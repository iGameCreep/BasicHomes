package fr.gamecreep.basichomes;

import dev.faststats.ErrorTracker;
import dev.faststats.bukkit.BukkitContext;
import dev.faststats.data.Metric;
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
        BukkitContext context = new BukkitContext.Factory(this.plugin, Constants.FASTSTATS_TOKEN)
                .errorTrackerService(ERROR_TRACKER)
        .metrics(factory ->
                factory
                .addMetric(Metric.bool("using_warps", this::getWarpMetricValue))
                .create()
        )
        .create();

        context.ready();
    }

    private boolean getWarpMetricValue() {
        Map<ConfigElement, Object> pluginConfig = plugin.getPluginConfig().getConfig();

        return (Boolean) pluginConfig.getOrDefault(ConfigElement.WARPS_ENABLED, false);
    }
}
