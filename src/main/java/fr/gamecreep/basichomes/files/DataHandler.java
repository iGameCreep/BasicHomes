package fr.gamecreep.basichomes.files;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.classes.SavedPosition;
import lombok.NonNull;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class DataHandler {
    private final DataStore dataStore;

    public DataHandler(BasicHomes plugin, String fileName) {
        this.dataStore = new DataStore(plugin, fileName);
    }

    public void create(@NonNull SavedPosition data) {
        List<SavedPosition> list = new LinkedList<>(this.dataStore.loadData());
        list.add(data);
        this.dataStore.saveData(list);
    }

    public void delete(@NonNull SavedPosition pos) {
        List<SavedPosition> list = new LinkedList<>(this.dataStore.loadData());
        list.remove(pos);
        this.dataStore.saveData(list);
    }

    public List<SavedPosition> getAll() {
        return this.dataStore.loadData();
    }

    public List<SavedPosition> getAllByPlayer(@NonNull Player player) {
        List<SavedPosition> list = new ArrayList<>();

        for (SavedPosition pos : this.getAll()) {
            if (UUID.fromString(pos.getOwnerUuid()).equals(player.getUniqueId())) list.add(pos);
        }

        return list;
    }

    @Nullable
    public SavedPosition getByName(@NonNull Player player, @NonNull String name) {
        List<SavedPosition> list = this.getAllByPlayer(player);

        for (SavedPosition pos : list) {
            if (pos.getName().equals(name)) return pos;
        }

        return null;
    }
}
