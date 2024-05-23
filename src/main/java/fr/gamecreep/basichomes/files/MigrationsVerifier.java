package fr.gamecreep.basichomes.files;

import com.google.gson.reflect.TypeToken;
import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.MigrationsData;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.PositionType;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

public class MigrationsVerifier extends DataStore<MigrationsData> {
    private static final int LATEST_MIGRATION = 2;
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
    private void v1() {
        super.plugin.getPluginLogger().logInfo("Running migration V1...");

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
        super.plugin.getPluginLogger().logInfo("Success!");
    }

    /**
     * Moves homes.json and warps.json to data.json
     */
    private void v2() {
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
        super.plugin.getPluginLogger().logInfo("Success!");
    }

    private void saveLatestMigration(final int migrationNumber) {
        MigrationsData data = new MigrationsData();
        data.setLatestMigrationNumberDone(migrationNumber);
        this.saveData(data);
    }

    private void callMigrationFunction(int migrationNumber) {
        switch (migrationNumber) {
            case 1:
                v1();
                break;
            case 2:
                v2();
                break;
            default:
                this.plugin.getPluginLogger().logWarning("Could not find migration for number " + migrationNumber);
                break;
        }
    }
}
