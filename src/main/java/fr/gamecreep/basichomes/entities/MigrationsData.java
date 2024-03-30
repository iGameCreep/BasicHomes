package fr.gamecreep.basichomes.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MigrationsData {
    private int latestMigrationNumberDone = 0;

    public MigrationsData(int latestMigrationNumberDone) {
        this.latestMigrationNumberDone = latestMigrationNumberDone;
    }

    public MigrationsData() { }
}
