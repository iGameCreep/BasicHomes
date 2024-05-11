package fr.gamecreep.basichomes.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MigrationsData {
    private int latestMigrationNumberDone = 0;

    public MigrationsData(final int latestMigrationNumberDone) {
        this.latestMigrationNumberDone = latestMigrationNumberDone;
    }
}
