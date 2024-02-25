package fr.gamecreep.basichomes.files;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.classes.Default;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataStore<T extends Default> {
    private final String fileName;
    private final Gson gson = new Gson();
    private final BasicHomes plugin;

    public DataStore(BasicHomes plugin, String fileName) {
        this.fileName = "plugins" + File.separator + "BasicHomes" + File.separator + fileName;
        this.plugin = plugin;
        verifyFiles();
    }

    private void verifyFiles() {
        File file = new File(this.fileName);
        final String errorMessage = String.format("Could not create the data store file %s.", this.fileName);

        try {
            File parentDir = file.getParentFile();

            if (!parentDir.exists() && !parentDir.mkdirs()) {
                this.plugin.getPluginLogger().logWarning("Could not create the parent directory.");
                return;
            }
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    this.plugin.getPluginLogger().logWarning(errorMessage);
                    return;
                }
                this.saveData(Collections.emptyList());
            }
        } catch (IOException e) {
            this.plugin.getPluginLogger().logWarning(errorMessage);
        }
    }

    public void saveData(List<T> list) {
        try {
            FileWriter writer = new FileWriter(this.fileName);
            this.gson.toJson(list, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            plugin.getPluginLogger().logWarning(String.format("Could not save data to file %s", this.fileName));
        }
    }

    public List<T> loadData() {
        try {
            Type listType = new TypeToken<ArrayList<T>>(){}.getType();
            List<T> list = gson.fromJson(new FileReader(this.fileName), listType);
            return list == null ? Collections.emptyList() : list;
        } catch (IOException e) {
            plugin.getPluginLogger().logWarning(String.format("Could not load data from file %s", this.fileName));
            return Collections.emptyList();
        }
    }
}
