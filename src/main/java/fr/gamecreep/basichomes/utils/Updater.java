package fr.gamecreep.basichomes.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.gamecreep.basichomes.BasicHomes;
import fr.gamecreep.basichomes.exceptions.BasicHomesException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Updater {

    private static final String UPDATE_URL = "https://basichomes.netlify.app/.netlify/functions/version";
    private final Gson gson = new Gson();

    @Nullable
    public UpdateData checkForUpdates() throws BasicHomesException {
        try {
            final URL url = new URL(UPDATE_URL);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final JsonObject response = gson.fromJson(reader, JsonObject.class);
            reader.close();

            final String downloadUrl = response.get("download_url").getAsString();
            final String latestVersion = response.get("latest_version").getAsString();
            final String currentVersion = BasicHomes.PLUGIN_VERSION;

            if (!currentVersion.equals(latestVersion)) {
                return new UpdateData(latestVersion, downloadUrl);
            } else {
                return null;
            }
        } catch (final Exception e) {
            throw new BasicHomesException("Error checking for updates: " + e.getMessage());
        }
    }

    @AllArgsConstructor
    @Getter
    public static class UpdateData {
        private String latestVersion;
        private String downloadUrl;
    }
}
