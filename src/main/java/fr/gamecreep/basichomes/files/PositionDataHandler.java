package fr.gamecreep.basichomes.files;

import com.google.gson.reflect.TypeToken;
import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import lombok.NonNull;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PositionDataHandler {

    private static final Type TYPE = new TypeToken<List<SavedPosition>>(){}.getType();
    private final DataStore<List<SavedPosition>> dataStore;

    public PositionDataHandler(@NonNull final BasicHomes plugin, final String fileName) {
        this.dataStore = new DataStore<>(plugin, fileName, Collections.emptyList());
    }

    public void create(@NonNull final SavedPosition pos) {
        final List<SavedPosition> list = this.dataStore.loadData(TYPE);
        list.add(pos);
        this.dataStore.saveData(list);
    }

    public void delete(@NonNull final SavedPosition pos) {
        final List<SavedPosition> list = this.dataStore.loadData(TYPE);

        list.removeIf(savedPos -> savedPos.getId().equals(pos.getId()));

        this.dataStore.saveData(list);
    }

    public List<SavedPosition> getAll(final PositionType type) {
        List<SavedPosition> all = this.dataStore.loadData(TYPE);
        List<SavedPosition> correctType = new ArrayList<>();

        for (final SavedPosition pos : all) {
            if (pos.getType().equals(type)) correctType.add(pos);
        }

        return correctType;
    }

    public List<SavedPosition> getAllByPlayer(@NonNull final PositionType type, @NonNull final Player player) {
        if (type.equals(PositionType.WARP)) return this.getAll(type);
        final List<SavedPosition> list = new ArrayList<>();

        for (final SavedPosition pos : this.getAll(type)) {
            if (UUID.fromString(pos.getOwnerUuid()).equals(player.getUniqueId())) list.add(pos);
        }

        return list;
    }

    @Nullable
    public SavedPosition getByName(@NonNull final PositionType type, @NonNull final Player player, final String name) {
        final List<SavedPosition> list = this.getAllByPlayer(type, player);

        for (final SavedPosition pos : list) {
            if (pos.getName().equals(name)) return pos;
        }

        return null;
    }

    @Nullable
    public SavedPosition getByName(@NonNull final PositionType type, final String name) {
        final List<SavedPosition> list = this.getAll(type);

        for (final SavedPosition pos : list) {
            if (pos.getName().equals(name)) return pos;
        }

        return null;
    }

    @Nullable
    public SavedPosition getById(@NonNull final PositionType type, @NonNull final Player player, final UUID id) {
        for (final SavedPosition pos : this.getAllByPlayer(type, player)) {
            if (pos.getId().equals(id)) return pos;
        }

        return null;
    }

    @Nullable
    public SavedPosition getById(@NonNull final PositionType type, final UUID id) {
        for (final SavedPosition pos : this.getAll(type)) {
            if (pos.getId().equals(id)) return pos;
        }

        return null;
    }
}
