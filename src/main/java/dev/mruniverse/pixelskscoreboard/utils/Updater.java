package dev.mruniverse.pixelskscoreboard.utils;

import dev.mruniverse.pixelskscoreboard.PixelSkScoreboard;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class Updater {
    private final String currentVersion;

    private String newestVersion;

    public Updater(PixelSkScoreboard main, int projectID) {
        currentVersion = main.getDescription().getVersion();
        try {
            URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + projectID);
            HttpsURLConnection connection;
            connection = (HttpsURLConnection) url.openConnection();
            connection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            newestVersion = sb.toString();
        } catch (Throwable throwable) {
            main.getLogs().error("Can't connect to WiFi services!");
        }
    }
    public String getVersionResult() {
        String update;
        String[] installed;
        if(currentVersion == null) {
            return "RED_PROBLEM";
        }
        update = currentVersion;
        if(currentVersion.contains(".")) update= currentVersion.replace(".","");
        installed= update.split("-");
        if(installed[1] != null) {
            if(installed[1].toLowerCase().contains("pre")) {
                if(installed[1].toLowerCase().contains("alpha")) {
                    return "PRE_ALPHA_VERSION";
                }
                return "PRE_RELEASE";
            }
            if(installed[1].toLowerCase().contains("alpha")) {
                return "ALPHA_VERSION";
            }
            if(installed[1].toLowerCase().contains("release")) {
                return "RELEASE";
            }
        }
        return "RELEASE";
    }
    public String getUpdateResult() {
        int using,latest;
        String update;
        String[] installed, spigot;
        //Version Verificator

        if(currentVersion == null || newestVersion == null) {
            return "RED_PROBLEM";
        }

        //Version Setup

        //* First Setup

        update= currentVersion;
        if(currentVersion.contains(".")) update= currentVersion.replace(".","");
        installed= update.split("-");
        update= newestVersion;
        if(newestVersion.contains(".")) update= newestVersion.replace(".","");
        spigot= update.split("-");

        //* Second Setup

        using= Integer.parseInt(installed[0]);
        latest= Integer.parseInt(spigot[0]);

        //Result Setup
        if(using == latest) {
            if(installed[1].equalsIgnoreCase(spigot[1])) {
                return "UPDATED";
            }
            return "NEW_VERSION";
        }
        if(using < latest) {
            return "NEW_VERSION";
        }
        if(installed[1].toLowerCase().contains("pre")) {
            if(installed[1].toLowerCase().contains("alpha")) {
                return "PRE_ALPHA_VERSION";
            }
            return "BETA_VERSION";
        }
        if(installed[1].toLowerCase().contains("alpha")) {
            return "ALPHA_VERSION";
        }
        return "RED_PROBLEM";
    }
}
