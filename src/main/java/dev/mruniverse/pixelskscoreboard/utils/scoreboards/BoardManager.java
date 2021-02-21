package dev.mruniverse.pixelskscoreboard.utils.scoreboards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class BoardManager {
    private final HashMap<UUID, PlayerManager> players = new HashMap<>();

    public PlayerManager getBoardOfPlayer(Player player) {
        return players.get(player.getUniqueId());
    }

    public void removeScore(Player player) {
        players.remove(player.getUniqueId());
    }

    public void setTitle(Player player,String title){
        if(!existPlayer(player)) {
            players.put(player.getUniqueId(), new PlayerManager(player));
        }
        PlayerManager scoreboard = getBoardOfPlayer(player);
        title = ChatColor.translateAlternateColorCodes('&',title);
        scoreboard.setTitle(title);
    }
    public void setLines(Player player, List<String> lines){
        if(!existPlayer(player)) {
            players.put(player.getUniqueId(), new PlayerManager(player));
        }
        PlayerManager scoreboard = getBoardOfPlayer(player);
        scoreboard.updateLines(lines);
    }
    public void setSlot(Player player,int slot,String text){
        if(!existPlayer(player)) {
            players.put(player.getUniqueId(), new PlayerManager(player));
        }
        PlayerManager scoreboard = getBoardOfPlayer(player);
        text = ChatColor.translateAlternateColorCodes('&',text);
        scoreboard.setSlot(slot,text);
    }
    private boolean existPlayer(Player player) {
        return players.containsKey(player.getUniqueId());
    }

}
