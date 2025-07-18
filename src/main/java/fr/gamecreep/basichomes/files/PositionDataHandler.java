package fr.gamecreep.basichomes.files;

import com.google.gson.reflect.TypeToken;
import fr.gamecreep.basichomes.entities.SavedPosition;
import fr.gamecreep.basichomes.entities.enums.PositionType;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PositionDataHandler {

    @Getter
    private final DataStore<List<SavedPosition>> dataStore;

    public PositionDataHandler(@NonNull final String fileName) {
        this.dataStore = new DataStore<>(fileName, new ArrayList<>(), new TypeToken<List<SavedPosition>>(){}.getType());
    }

    public void create(@NonNull final SavedPosition pos) {
        final List<SavedPosition> list = this.dataStore.getData();
        list.add(pos);
        this.dataStore.save();
    }

    public void delete(@NonNull final SavedPosition pos) {
        final List<SavedPosition> list = this.dataStore.getData();

        list.removeIf(savedPos -> savedPos.getId().equals(pos.getId()));

        this.dataStore.save();
    }

    public List<SavedPosition> getAllUnfiltered() {
        return this.dataStore.getData();
    }

    public List<SavedPosition> getAll(@NonNull final PositionType type) {
        final List<SavedPosition> all = this.dataStore.getData();
        final List<SavedPosition> correctType = new ArrayList<>();

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
