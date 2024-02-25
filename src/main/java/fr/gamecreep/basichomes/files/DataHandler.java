package fr.gamecreep.basichomes.files;

import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.classes.Default;
import lombok.NonNull;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class DataHandler<T extends Default> {
    private final DataStore<T> dataStore;

    public DataHandler(BasicHomes plugin, String fileName) {
        this.dataStore = new DataStore<>(plugin, fileName);
    }

    public void create(@NonNull T data) {
        List<T> list = new LinkedList<>(this.dataStore.loadData());
        list.add(data);
        this.dataStore.saveData(list);
    }

    public void delete(@NonNull T data) {
        List<T> list = new LinkedList<>(this.dataStore.loadData());
        list.remove(data);
        this.dataStore.saveData(list);
    }

    public List<T> getAll() {
        return this.dataStore.loadData();
    }

    public List<T> getAllByPlayer(@NonNull Player player) {
        List<T> list = new ArrayList<>();

        for (T data : this.getAll()) {
            if (UUID.fromString(data.getOwnerUuid()).equals(player.getUniqueId())) list.add(data);
        }

        return list;
    }

    @Nullable
    public T getByName(@NonNull Player player, @NonNull String name) {
        List<T> list = this.getAllByPlayer(player);

        for (T data : list) {
            if (data.getName().equals(name)) return data;
        }

        return null;
    }
}
