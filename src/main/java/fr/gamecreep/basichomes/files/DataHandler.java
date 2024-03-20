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
        SavedPosition toRemove = this.getByIdInList(list, pos.getId());
        list.remove(toRemove);
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

    @Nullable
    public SavedPosition getByName(@NonNull String name) {
        List<SavedPosition> list = this.getAll();

        for (SavedPosition pos : list) {
            if (pos.getName().equals(name)) return pos;
        }

        return null;
    }

    @Nullable
    public SavedPosition getById(@NonNull final Player player, @NonNull final UUID id) {
        for (SavedPosition pos : this.getAllByPlayer(player)) {
            if (pos.getId().equals(id)) return pos;
        }
        return null;
    }

    @Nullable
    public SavedPosition getById(@NonNull final UUID id) {
        for (SavedPosition pos : this.getAll()) {
            if (pos.getId().equals(id)) return pos;
        }
        return null;
    }

    @Nullable
    private SavedPosition getByIdInList(List<SavedPosition> list, UUID id) {
        for (SavedPosition pos : list) {
            if (pos.getId().equals(id)) return pos;
        }
        return null;
    }
}
