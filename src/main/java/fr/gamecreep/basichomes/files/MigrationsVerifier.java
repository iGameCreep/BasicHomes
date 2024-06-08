package fr.gamecreep.basichomes.files;

import com.google.gson.reflect.TypeToken;
import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.MigrationsData;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.utils.PositionUtils;
import org.bukkit.Material;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MigrationsVerifier extends DataStore<MigrationsData> {
    private static final int LATEST_MIGRATION = 3;
    private static final Type TYPE = new TypeToken<MigrationsData>(){}.getType();
    private static final String HOMES_FILE_NAME = "homes.json";
    private static final String WARPS_FILE_NAME = "warps.json";

    public MigrationsVerifier(final BasicHomes plugin) {
        super(plugin, "migrations.json", new MigrationsData(0));
    }

    public void verifyMigrations() {
        int lastMigrationDone = this.getLastMigrationDone();
        if (lastMigrationDone < LATEST_MIGRATION) {
            for (int i = lastMigrationDone + 1; i <= LATEST_MIGRATION; i++) {
                callMigrationFunction(i);
            }
        }
    }

    private int getLastMigrationDone() {
        return this.loadData(TYPE).getLatestMigrationNumberDone();
    }

    /**
     * Adds PositionType to saved positions directly in files.
     */
    private boolean v1() {
        final PositionDataHandler homeHandler = new PositionDataHandler(super.plugin, HOMES_FILE_NAME);
        final PositionDataHandler warpHandler = new PositionDataHandler(super.plugin, WARPS_FILE_NAME);

        final List<SavedPosition> homeList = homeHandler.getAll(PositionType.HOME);
        final List<SavedPosition> warpList = warpHandler.getAll(PositionType.WARP);

        for (final SavedPosition savedPosition : homeList) {
            homeHandler.delete(savedPosition);
            savedPosition.setType(PositionType.HOME);
            homeHandler.create(savedPosition);
        }

        for (final SavedPosition savedPosition : warpList) {
            warpHandler.delete(savedPosition);
            savedPosition.setType(PositionType.WARP);
            warpHandler.create(savedPosition);
        }

        this.saveLatestMigration(1);
        return true;
    }

    /**
     * Moves homes.json and warps.json to data.json
     */
    private boolean v2() {
        super.plugin.getPluginLogger().logInfo("Running migration V2...");

        final PositionDataHandler positionDataHandler = super.plugin.getPositionDataHandler();
        final PositionDataHandler homeHandler = new PositionDataHandler(super.plugin, HOMES_FILE_NAME);
        final PositionDataHandler warpHandler = new PositionDataHandler(super.plugin, WARPS_FILE_NAME);

        final List<SavedPosition> homeList = homeHandler.getAll(PositionType.HOME);
        final List<SavedPosition> warpList = warpHandler.getAll(PositionType.WARP);

        for (final SavedPosition savedPosition : homeList) {
            positionDataHandler.create(savedPosition);
        }

        for (final SavedPosition savedPosition : warpList) {
            positionDataHandler.create(savedPosition);
        }

        final File homesfile = new File("plugins" + File.separator + "BasicHomes" + File.separator + HOMES_FILE_NAME);
        if (homesfile.exists()) homesfile.deleteOnExit();

        final File warpsFile = new File("plugins" + File.separator + "BasicHomes" + File.separator + WARPS_FILE_NAME);
        if (warpsFile.exists()) warpsFile.deleteOnExit();

        this.saveLatestMigration(2);
        return true;
    }

    /**
     * Add a material to each saved position
     */
    private boolean v3() {
        final PositionDataHandler handler = this.plugin.getPositionDataHandler();

        final List<SavedPosition> all = new ArrayList<>();

        all.addAll(handler.getAll(PositionType.HOME));
        all.addAll(handler.getAll(PositionType.WARP));

        for (final SavedPosition pos : all) {
            handler.delete(pos);

            try {
                final Material material = PositionUtils.getMaterialFromWorldEnvironment(Objects.requireNonNull(pos.getLocation().getWorld()).getEnvironment());
                pos.setBlock(material);
            } catch (final NullPointerException e) {
                this.plugin.getPluginLogger().logWarning("Unable to run migration V3: " + e);
                return false;
            }

            handler.create(pos);
        }

        this.saveLatestMigration(3);
        return true;
    }

    private void saveLatestMigration(final int migrationNumber) {
        final MigrationsData data = new MigrationsData();
        data.setLatestMigrationNumberDone(migrationNumber);
        this.saveData(data);
    }

    private void callMigrationFunction(final int migrationNumber) {
        super.plugin.getPluginLogger().logInfo(String.format("Running migration V%s...", migrationNumber));

        boolean success = switch (migrationNumber) {
            case 1 -> v1();
            case 2 -> v2();
            case 3 -> v3();
            default -> migrationNotFound(migrationNumber);
        };

        if (success) {
            super.plugin.getPluginLogger().logInfo("Success!");
        } else {
            super.plugin.getPluginLogger().logWarning("A migration has failed to run. The plugin may not work properly. Please reload/restart the server. If this error persists, contact a plugin developer.");
        }
    }

    private boolean migrationNotFound(final int migrationNumber) {
        this.plugin.getPluginLogger().logWarning("Could not find migration for number " + migrationNumber);
        return true;
    }
}
