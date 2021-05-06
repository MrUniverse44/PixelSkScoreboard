package dev.mruniverse.pixelskscoreboard.utils.scoreboards;
import dev.mruniverse.pixelskscoreboard.netherboard.BPlayerBoard;
import dev.mruniverse.pixelskscoreboard.netherboard.Netherboard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class PacketManager {
    private final Netherboard netherboard = new Netherboard();
    private final HashMap<UUID, BPlayerBoard> players = new HashMap<>();

    public BPlayerBoard getBoardOfPlayer(Player player) {
        return players.get(player.getUniqueId());
    }

    public BPlayerBoard getToAdd(Player player) {
        if(!existPlayer(player)) {
            players.put(player.getUniqueId(), netherboard.createBoard(player,"guardianBoard"));
        }
        return players.get(player.getUniqueId());
    }

    public void removeScore(Player player) {
        players.remove(player.getUniqueId());
    }

    public void setTitle(Player player,String title){
        if(!existPlayer(player)) {
            players.put(player.getUniqueId(), netherboard.createBoard(player,"guardianBoard"));
        }
        BPlayerBoard scoreboard = getBoardOfPlayer(player);
        scoreboard.setName(ChatColor.translateAlternateColorCodes('&',title));
    }
    public void setLines(Player player, List<String> lines){
        if(!existPlayer(player)) {
            players.put(player.getUniqueId(), netherboard.createBoard(player,"guardianBoard"));
        }
        BPlayerBoard scoreboard = getBoardOfPlayer(player);
        String[] array = new String[lines.size()];
        lines.toArray(array);
        scoreboard.setAll(array);
    }
    public void setSlot(Player player,int slot,String text){
        if(!existPlayer(player)) {
            players.put(player.getUniqueId(), netherboard.createBoard(player,"guardianBoard"));
        }
        BPlayerBoard scoreboard = getBoardOfPlayer(player);
        text = ChatColor.translateAlternateColorCodes('&',text);
        scoreboard.set(ChatColor.translateAlternateColorCodes('&',text),slot);
    }
    public void hideBoard(Player player) {
        if(!existPlayer(player)) {
            players.put(player.getUniqueId(), netherboard.createBoard(player,"guardianBoard"));
        }
        BPlayerBoard scoreboard = getBoardOfPlayer(player);
        scoreboard.delete();
    }
    @SuppressWarnings("unused")
    public void deletePlayer(Player player) {
        if(existPlayer(player)) {
            players.remove(player.getUniqueId());
        }
    }
    private boolean existPlayer(Player player) {
        return players.containsKey(player.getUniqueId());
    }

}