package dev.mruniverse.pixelskscoreboard.listeners;

import dev.mruniverse.pixelskscoreboard.PixelSkScoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    private final PixelSkScoreboard plugin;
    public PlayerListener(PixelSkScoreboard main) {
        plugin = main;
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(plugin.getStorage().getSettings().getBoolean("settings.update-check")) {
            if(event.getPlayer().hasPermission("pixelskscoreboard.update-notify")) {
                if(plugin.hasNewVersion()) {
                    plugin.getStorage().getMessages().getString("messages.newUpdate");
                }
            }
        }
    }
}
