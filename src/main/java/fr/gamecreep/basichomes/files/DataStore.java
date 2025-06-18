package fr.gamecreep.basichomes.files;

import com.google.gson.Gson;
import fr.gamecreep.basichomes.utils.LoggerUtils;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

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
        this.data = loadOrCreate();
    }

    private T loadOrCreate() {
        if (!this.file.exists()) {
            saveData(this.defaultData);
            return this.defaultData;
        }

        try (final FileReader reader = new FileReader(file)) {
            return this.gson.fromJson(reader, this.type);
        } catch (IOException e) {
            LoggerUtils.logWarning("Failed to load data from " + this.file.getName());
            return this.defaultData;
        }
    }

    public void save() {
        this.saveData(this.data);
    }

    private void saveData(final T data) {
        try (final FileWriter writer = new FileWriter(this.file)) {
            this.gson.toJson(data, writer);
        } catch (IOException e) {
            LoggerUtils.logWarning("Failed to save data to " + this.file.getName());
        }
    }
}
