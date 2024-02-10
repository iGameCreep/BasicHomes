package fr.gamecreep.basichomes.files;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.homes.PlayerHome;
import lombok.NonNull;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataHandler {
    private final DataStore dataStore;

    public DataHandler(BasicHomes plugin) {
        this.dataStore = new DataStore(plugin);
    }

    public void createHome(@NonNull PlayerHome home) {
        List<PlayerHome> homes = this.dataStore.loadData();
        homes.add(home);
        this.dataStore.saveData(homes);
    }

    public void deleteHome(@NonNull PlayerHome home) {
        List<PlayerHome> homes = this.dataStore.loadData();
        homes.remove(home);
        this.dataStore.saveData(homes);
    }

    public List<PlayerHome> getAllHomes() {
        return this.dataStore.loadData();
    }

    public List<PlayerHome> getAllPlayerHomes(@NonNull Player player) {
        List<PlayerHome> homes = new ArrayList<>();

        for (PlayerHome home : this.getAllHomes()) {
            if (UUID.fromString(home.getOwnerUuid()).equals(player.getUniqueId())) homes.add(home);
        }

        return homes;
    }

    @Nullable
    public PlayerHome getHomeByName(@NonNull Player player, @NonNull String name) {
        List<PlayerHome> homes = this.getAllPlayerHomes(player);

        for (PlayerHome home : homes) {
            if (home.getName().equals(name)) return home;
        }

        return null;
    }
}
