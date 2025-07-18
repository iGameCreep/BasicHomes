package fr.gamecreep.basichomes.files;

import com.google.gson.Gson;
import fr.gamecreep.basichomes.utils.LoggerUtils;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataStore<T> {
    private final File file;
    private final Gson gson = new Gson();
    private final T defaultData;
    private final Type type;
    @Getter
    private T data;

    public DataStore(final String fileName, final T defaultData, final Type type) {
        this.file = new File("plugins" + File.separator + "BasicHomes", fileName);
        this.defaultData = defaultData;
        this.type = type;
        this.data = this.loadOrCreate();
    }

    @SuppressWarnings("unchecked")
    private T wrapMutable(T data) {
        if (data instanceof List) {
            return (T) new ArrayList<>((List<?>) data);
        }
        return data;
    }

    private T loadOrCreate() {
        if (!this.file.exists()) {
            this.saveData(this.defaultData);
            return this.wrapMutable(this.defaultData);
        }

        try (final FileReader reader = new FileReader(this.file)) {
            T loadedData = this.gson.fromJson(reader, this.type);
            return this.wrapMutable(loadedData);
        } catch (IOException e) {
            LoggerUtils.logWarning("Failed to load data from " + this.file.getName());
            return this.defaultData;
        }
    }

    public void save() {
        this.saveData(this.data);
    }

    private void saveData(final T data) {
        final File parent = this.file.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            LoggerUtils.logWarning("Failed to create directories for " + parent.getPath());
            return;
        }

        try (final FileWriter writer = new FileWriter(this.file)) {
            this.gson.toJson(data, writer);
        } catch (IOException e) {
            LoggerUtils.logWarning("Failed to save data to " + this.file.getName());
        }
    }
}
