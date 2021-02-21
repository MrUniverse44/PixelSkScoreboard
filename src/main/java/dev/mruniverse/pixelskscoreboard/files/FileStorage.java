package dev.mruniverse.pixelskscoreboard.files;

import dev.mruniverse.pixelskscoreboard.PixelSkScoreboard;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;


public class FileStorage {
    private final PixelSkScoreboard plugin;
    private final File rxSettings;
    private final File rxMessages;
    private final File rxPlugins;
    private final File rxMain;
    private FileConfiguration settings, messages;

    public FileStorage(PixelSkScoreboard main) {
        plugin = main;
        rxSettings = new File(main.getDataFolder(), "settings.yml");
        rxMessages = new File(main.getDataFolder(), "messages.yml");
        rxPlugins = main.getDataFolder().getParentFile();
        rxMain = rxPlugins.getParentFile();
        settings = loadConfig("settings");
        messages = loadConfig("messages");
    }

    /**
     * Creates a config File if it doesn't exists,
     * reloads if specified file exists.
     *
     * @param configName config to create/reload.
     */
    public FileConfiguration loadConfig(String configName) {
        File configFile = new File(plugin.getDataFolder(), configName + ".yml");

        if (!configFile.exists()) {
            saveConfig(configName);
        }

        FileConfiguration cnf = null;
        try {
            cnf = YamlConfiguration.loadConfiguration(configFile);
        } catch (Exception e) {
            plugin.getLogs().warn(String.format("A error occurred while loading the settings file. Error: %s", e));
            e.printStackTrace();
        }

        plugin.getLogs().info(String.format("&7File &e%s.yml &7has been loaded", configName));
        return cnf;
    }
    /**
     * Get External Fle Control,
     * reloads if specified file exists.
     *
     * @param fileLocation config to get/create.
     */
    public File getExternalFile(String fileLocation) {
        String[] config = fileLocation.split("/");
        String file = config[config.length - 1];
        String pre = fileLocation.replace(file, "");
        File configFile;
        if(pre.equals(""))  {
            configFile = new File(getServerFolder(),file);
        } else {
            configFile = new File(pre,file);
        }
        if (!configFile.exists()) {
            try {
                boolean create = configFile.createNewFile();
                if(create) plugin.getLogs().debug("File &3" + file + " &7created.");
            } catch (Throwable throwable) {
                plugin.getLogs().error("Can't create file: " + file);
                plugin.getLogs().error(throwable);
            }
        }
        return configFile;
    }
    /**
     * Creates a config File if it doesn't exists,
     * reloads if specified file exists.
     *
     * @param file config to control/reload.
     */
    public FileConfiguration loadExternalConfigWithFile(File file) {
        FileConfiguration cnf = null;
        try {
            cnf = YamlConfiguration.loadConfiguration(file);
        } catch (Throwable throwable) {
            plugin.getLogs().error("Can't load your config: " + file.getName());
            plugin.getLogs().error(throwable);
        }

        plugin.getLogs().info(String.format("&7File &e%s &7has been loaded", file.getName()));
        return cnf;
    }
    /**
     * Creates a config File if it doesn't exists,
     * reloads if specified file exists.
     *
     * @param configName config to create/reload.
     */
    public FileConfiguration loadExternalConfig(String configName) {
        String[] config = configName.split("/");
        String file = config[config.length - 1];
        String pre = configName.replace(file, "");
        File configFile;
        if(pre.equals(""))  {
            configFile = new File(getServerFolder(),file);
        } else {
            pre = pre.replace("[serverDir]/","");
            File preTwo = new File(getServerFolder() + pre);
            configFile = new File(preTwo,file);
        }

        if (!configFile.exists()) {
            try {
                boolean create = configFile.createNewFile();
                if(create) plugin.getLogs().debug("File &3" + file + " &7created.");
            } catch (Throwable throwable) {
                plugin.getLogs().error("Can't create file: " + file);
                plugin.getLogs().error(throwable);
            }
        }
        try {
            plugin.getLogs().info(String.format("&7File &e%s.yml &7has been loaded correctly", configName));
            return YamlConfiguration.loadConfiguration(configFile);
        } catch (Throwable throwable) {
            plugin.getLogs().error("Can't load your config: " + configName);
            plugin.getLogs().error(throwable);
        }
        return null;
    }
    /**
     * Save config File Changes & Paths
     *
     * @param configName config to save/create.
     */
    public void saveConfig(String configName) {
        File folderDir = plugin.getDataFolder();
        File file = new File(plugin.getDataFolder(), configName + ".yml");
        if (!folderDir.exists()) {
            boolean createFile = folderDir.mkdir();
            if(createFile) plugin.getLogs().info("&7Folder created!");
        }

        if (!file.exists()) {
            try (InputStream in = plugin.getResource(configName + ".yml")) {
                if(in != null) {
                    Files.copy(in, file.toPath());
                }
            } catch (Throwable throwable) {
                plugin.getLogs().error(String.format("A error occurred while copying the config %s to the plugin data folder. Error: %s", configName, throwable));
                plugin.getLogs().error(throwable);
            }
        }
    }

    public void saveExternal(String ExternalFile) {
        String[] config = ExternalFile.split("/");
        String file = config[config.length - 1];
        String pre = ExternalFile.replace(file, "");
        File configFile;
        if(pre.equals(""))  {
            configFile = new File(getServerFolder(),file);
        } else {
            configFile = new File(pre,file);
        }
        if (!configFile.exists()) {
            try {
                boolean create = configFile.createNewFile();
                if(create) plugin.getLogs().debug("File &3" + file + " &7created.");
            } catch (Throwable throwable) {
                plugin.getLogs().error("Can't create file: " + file);
                plugin.getLogs().error(throwable);
            }
        }
        try {
            loadExternalConfig(ExternalFile).save(configFile);
        }catch (Throwable throwable) {
            plugin.getLogs().error("Can't save settings file.");
            plugin.getLogs().error(throwable);
        }
    }

    public void reload() {
        settings = loadConfig("settings");
        messages = loadConfig("messages");
    }
    public void saveSettings() {
        try {
            getSettings().save(rxSettings);
        }catch (Throwable throwable) {
            plugin.getLogs().error("Can't save settings file.");
            plugin.getLogs().error(throwable);
        }
    }
    public void saveMessages() {
        try {
            getMessages().save(rxMessages);
        }catch (Throwable throwable) {
            plugin.getLogs().error("Can't save messages file.");
            plugin.getLogs().error(throwable);
        }
    }


    /**
     * Get Files
     *
     * @return File
     */
    public File getPluginsFolder() {
        return rxPlugins;
    }
    public File getServerFolder() {
        return rxMain;
    }
    /**
     * Get Files
     *
     * @return FileConfiguration
     */
    public FileConfiguration getSettings() {
        if(settings == null) reload();
        return settings;
    }
    public FileConfiguration getMessages() {
        if(messages == null) reload();
        return messages;
    }
}
