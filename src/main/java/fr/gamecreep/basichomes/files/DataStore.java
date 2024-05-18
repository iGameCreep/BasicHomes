package fr.gamecreep.basichomes.files;

import com.google.gson.Gson;
import fr.gamecreep.basichomes.BasicHomes;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class DataStore<T> {
    private final String fileName;
    private final Gson gson = new Gson();
    protected final BasicHomes plugin;
    protected final T defaultData;

    public DataStore(final BasicHomes plugin, final String fileName, final T defaultData) {
        this.fileName = "plugins" + File.separator + "BasicHomes" + File.separator + fileName;
        this.plugin = plugin;
        this.defaultData = defaultData;
        verifyFiles();
    }

    private void verifyFiles() {
        final File file = new File(this.fileName);
        final String errorMessage = String.format("Could not create the data store file %s.", this.fileName);

        try {
            final File parentDir = file.getParentFile();

            if (!parentDir.exists() && !parentDir.mkdirs()) {
                this.plugin.getPluginLogger().logWarning("Could not create the parent directory.");
                return;
            }
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    this.plugin.getPluginLogger().logWarning(errorMessage);
                    return;
                }
                this.saveData(this.defaultData);
            }
        } catch (IOException e) {
            this.plugin.getPluginLogger().logWarning(errorMessage);
        }
    }

    public void saveData(final T data) {
        try {
            final FileWriter writer = new FileWriter(this.fileName);

            this.gson.toJson(data, writer);

            writer.flush();
            writer.close();
        } catch (IOException e) {
            plugin.getPluginLogger().logWarning(String.format("Could not save data to file %s", this.fileName));
        }
    }

    public T loadData(final Type type) {
        try {
            return gson.fromJson(new FileReader(this.fileName), type);
        } catch (IOException e) {
            plugin.getPluginLogger().logWarning(String.format("Could not load data from file %s", this.fileName));
            return null;
        }
    }
}
