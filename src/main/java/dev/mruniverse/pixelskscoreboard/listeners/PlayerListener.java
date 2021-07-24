package dev.mruniverse.pixelskscoreboard.listeners;

import dev.mruniverse.pixelskscoreboard.PixelSkScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(plugin.getScoreboards().existPlayer(player)) plugin.getScoreboards().removeScore(player);
        if(plugin.getPacketScoreboards().existPlayer(player)) plugin.getPacketScoreboards().removeScore(player);
    }
}
