package fr.gamecreep.basichomes.files;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.entities.homes.PlayerHome;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DataStore {
    private static final String FILENAME = "plugins\\BasicHomes\\homes.json";
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
        String data = gson.toJson(homes);

        try (BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(Files.newOutputStream(Paths.get(FILENAME))))) {
            out.writeUTF(data);
        } catch (IOException e) {
            plugin.getPluginLogger().logWarning(String.format("Could not save data to file %s", FILENAME));
        }
    }

    public List<PlayerHome> loadData() {
        try (BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(Files.newInputStream(Paths.get(FILENAME))))) {
            String data = in.readUTF();
            Type listType = new TypeToken<ArrayList<PlayerHome>>(){}.getType();
            return gson.fromJson(data, listType);
        } catch (IOException e) {
            plugin.getPluginLogger().logWarning(String.format("Could not load data from file %s", FILENAME));
            return Collections.emptyList();
        }
    }
}
