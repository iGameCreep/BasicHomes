package fr.gamecreep.basichomes.files;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.homes.PlayerHome;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataStore {
    private static final String FILENAME = "plugins" + File.separator + "BasicHomes" + File.separator + "homes.json";
    private final Gson gson = new Gson();
    private final BasicHomes plugin;

    public DataStore(BasicHomes plugin) {
        this.plugin = plugin;
        verifyFiles();
    }

    private void verifyFiles() {
        File file = new File(FILENAME);
        final String errorMessage = "Could not create the home file.";

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

    public void saveData(List<PlayerHome> homes) {
        try {
            FileWriter writer = new FileWriter(FILENAME);
            this.gson.toJson(homes, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            plugin.getPluginLogger().logWarning(String.format("Could not save data to file %s", FILENAME));
        }
    }

    public List<PlayerHome> loadData() {
        try {
            Type listType = new TypeToken<ArrayList<PlayerHome>>(){}.getType();
            List<PlayerHome> list = gson.fromJson(new FileReader(FILENAME), listType);
            return list == null ? Collections.emptyList() : list;
        } catch (IOException e) {
            plugin.getPluginLogger().logWarning(String.format("Could not load data from file %s", FILENAME));
            return Collections.emptyList();
        }
    }
}
