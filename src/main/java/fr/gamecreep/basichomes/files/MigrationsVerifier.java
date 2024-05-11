package fr.gamecreep.basichomes.files;

import com.google.gson.reflect.TypeToken;
import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.MigrationsData;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.PositionType;

import java.lang.reflect.Type;
import java.util.List;

public class MigrationsVerifier extends DataStore<MigrationsData> {
    private static final int LATEST_MIGRATION = 1;
    private static final Type TYPE = new TypeToken<MigrationsData>(){}.getType();

    public MigrationsVerifier(BasicHomes plugin) {
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
        PositionDataHandler homeHandler = super.plugin.getHomeHandler();
        PositionDataHandler warpHandler = super.plugin.getWarpHandler();
        List<SavedPosition> homeList = homeHandler.getAll();
        List<SavedPosition> warpList = warpHandler.getAll();

        for (SavedPosition savedPosition : homeList) {
            homeHandler.delete(savedPosition);
            savedPosition.setType(PositionType.HOME);
            homeHandler.create(savedPosition);
        }

        for (SavedPosition savedPosition : warpList) {
            warpHandler.delete(savedPosition);
            savedPosition.setType(PositionType.WARP);
            warpHandler.create(savedPosition);
        }

        this.saveLatestMigration(1);
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
            default:
                this.plugin.getPluginLogger().logWarning("Could not find migration for number " + migrationNumber);
                break;
        }
    }
}
