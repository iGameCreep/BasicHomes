package fr.gamecreep.basichomes.files;

import com.google.gson.reflect.TypeToken;
import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.MigrationsData;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import fr.gamecreep.basichomes.utils.LoggerUtils;
import fr.gamecreep.basichomes.utils.PositionUtils;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MigrationsVerifier {
    private static final int LATEST_MIGRATION = 3;
    private static final String HOMES_FILE_NAME = "homes.json";
    private static final String WARPS_FILE_NAME = "warps.json";

    private final DataStore<MigrationsData> dataStore;
    private final BasicHomes plugin;

    public MigrationsVerifier(final BasicHomes plugin) {
        this.dataStore = new DataStore<>(
                "migrations.json",
                new MigrationsData(0),
                new TypeToken<MigrationsData>(){}.getType()
        );
        this.plugin = plugin;
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
        return this.dataStore.getData().getLatestMigrationNumberDone();
    }

    /**
     * Adds PositionType to saved positions directly in files.
     */
    private boolean v1() {
        final PositionDataHandler homeHandler = new PositionDataHandler(HOMES_FILE_NAME);
        final PositionDataHandler warpHandler = new PositionDataHandler(WARPS_FILE_NAME);

        final List<SavedPosition> homeList = homeHandler.getAllUnfiltered();
        final List<SavedPosition> warpList = warpHandler.getAllUnfiltered();

        final List<SavedPosition> updatedHomes = new ArrayList<>();
        final List<SavedPosition> updatedWarps = new ArrayList<>();

        for (final SavedPosition savedPosition : homeList) {
            if (savedPosition == null) continue;
            savedPosition.setType(PositionType.HOME);
            updatedHomes.add(savedPosition);
        }

        for (final SavedPosition savedPosition : warpList) {
            if (savedPosition == null) continue;
            savedPosition.setType(PositionType.WARP);
            updatedWarps.add(savedPosition);
        }

        // Overwrite old data directly
        homeHandler.getDataStore().getData().clear();
        homeHandler.getDataStore().getData().addAll(updatedHomes);
        homeHandler.getDataStore().save();

        warpHandler.getDataStore().getData().clear();
        warpHandler.getDataStore().getData().addAll(updatedWarps);
        warpHandler.getDataStore().save();

        saveLatestMigration(1);
        return true;
    }

    /**
     * Moves homes.json and warps.json to data.json
     * Files will be removed on server stop
     */
    private boolean v2() {
        final PositionDataHandler positionDataHandler = this.plugin.getPositionDataHandler();
        final PositionDataHandler homeHandler = new PositionDataHandler(HOMES_FILE_NAME);
        final PositionDataHandler warpHandler = new PositionDataHandler(WARPS_FILE_NAME);

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
                LoggerUtils.logWarning("Unable to run migration V3: " + e);
                return false;
            }

            handler.create(pos);
        }

        this.saveLatestMigration(3);
        return true;
    }

    private void saveLatestMigration(final int migrationNumber) {
        this.dataStore.getData().setLatestMigrationNumberDone(migrationNumber);
        this.dataStore.save();
    }

    private void callMigrationFunction(final int migrationNumber) {
        LoggerUtils.logInfo(String.format("Running migration V%s...", migrationNumber));

        boolean success = switch (migrationNumber) {
            case 1 -> v1();
            case 2 -> v2();
            case 3 -> v3();
            default -> migrationNotFound(migrationNumber);
        };

        if (success) {
            LoggerUtils.logInfo("Success!");
        } else {
            LoggerUtils.logWarning("A migration has failed to run. The plugin may not work properly. Please reload/restart the server. If this error persists, contact a plugin developer.");
        }
    }

    private boolean migrationNotFound(final int migrationNumber) {
        LoggerUtils.logWarning("Could not find migration for number " + migrationNumber);
        return true;
    }
}
