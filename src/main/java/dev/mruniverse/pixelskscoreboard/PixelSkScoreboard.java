package dev.mruniverse.pixelskscoreboard;

import dev.mruniverse.pixelskscoreboard.files.FileStorage;
import dev.mruniverse.pixelskscoreboard.utils.Logger;
import dev.mruniverse.pixelskscoreboard.utils.Updater;
import org.bukkit.plugin.java.JavaPlugin;

public final class PixelSkScoreboard extends JavaPlugin {
    private Logger logger;
    private FileStorage fileStorage;

    @Override
    public void onEnable() {
        logger = new Logger(this);
        fileStorage = new FileStorage(this);
        if(getStorage().getSettings().getBoolean("settings.update-check")) {
            Updater updater = new Updater(this,80983);
            String updaterResult = updater.getUpdateResult();
            String versionResult = updater.getVersionResult();
            switch (updaterResult.toUpperCase()) {
                case "UPDATED":
                    getLogs().info("&aYou're using latest version of PixelSkScoreboard, You're Awesome!");
                    switch (versionResult.toUpperCase()) {
                        case "RED_PROBLEM":
                            getLogs().info("&aSkScoreboard can't connect to WiFi to check plugin version.");
                            break;
                        case "PRE_ALPHA_VERSION":
                            getLogs().info("&cYou are Running a &aPre Alpha version&c, it is normal to find several errors, please report these errors so that they can be solved. &eWARNING: &cI (MrUniverse) recommend a Stable version, PreAlpha aren't stable versions!");
                            break;
                        case "ALPHA_VERSION":
                            getLogs().info("&bYou are Running a &aAlpha version&b, it is normal to find several errors, please report these errors so that they can be solved.");
                            break;
                        case "RELEASE":
                            getLogs().info("&aYou are Running a &bRelease Version&a, this is a stable version, awesome!");
                            break;
                        case "PRE_RELEASE":
                            getLogs().info("&aYou are Running a &bPreRelease Version&a, this is a stable version but is not the final version or don't have finished all things of the final version, but is a stable version,awesome!");
                            break;
                        default:
                            break;
                    }
                    break;
                case "NEW_VERSION":
                    getLogs().info("&aA new update is available: &bhttps://www.spigotmc.org/resources/80983/");
                    break;
                case "BETA_VERSION":
                    getLogs().info("&aYou are Running a Pre-Release version, please report bugs ;)");
                    break;
                case "RED_PROBLEM":
                    getLogs().info("&aSkScoreboard can't connect to WiFi to check plugin version.");
                    break;
                case "ALPHA_VERSION":
                    getLogs().info("&bYou are Running a &aAlpha version&b, it is normal to find several errors, please report these errors so that they can be solved.");
                    break;
                case "PRE_ALPHA_VERSION":
                    getLogs().info("&cYou are Running a &aPre Alpha version&c, it is normal to find several errors, please report these errors so that they can be solved. &eWARNING: &cI (MrUniverse) recommend a Stable version, PreAlpha aren't stable versions!");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Public getLogs() from Plugin's Main class.
     * @return Logger
     */
    public Logger getLogs() {
        if(logger == null) logger = new Logger(this);
        return logger;
    }
    /**
     * Public getLogs() from Plugin's Main class.
     * @return Logger
     */
    public FileStorage getStorage() {
        if(fileStorage == null) fileStorage = new FileStorage(this);
        return fileStorage;
    }
}
