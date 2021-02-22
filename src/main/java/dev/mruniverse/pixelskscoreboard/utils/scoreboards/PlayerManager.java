package dev.mruniverse.pixelskscoreboard.utils.scoreboards;

import dev.mruniverse.pixelskscoreboard.PixelSkScoreboard;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

@SuppressWarnings("deprecation")
public class PlayerManager {
    private Scoreboard scoreboard;
    private Objective sidebar;
    private Player player;
    public PlayerManager(Player player) {
        if(Bukkit.getScoreboardManager() == null) return;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.player = player;
        this.sidebar = this.scoreboard.registerNewObjective("sidebar", "dummy");
        this.sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        for (int i = 1; i <= 15; i++) {
            Team team = this.scoreboard.registerNewTeam("SLOT_" + i);
            team.addEntry(genEntry(i));
        }
        player.setScoreboard(this.scoreboard);
        setTitle("&c ");
    }

    public void setTitle(String title) {
        PixelSkScoreboard board = PixelSkScoreboard.getControl();
        if(board.hasPAPI()) {
            title = PlaceholderAPI.setPlaceholders(player,ChatColor.translateAlternateColorCodes('&', title));
        } else {
            title = ChatColor.translateAlternateColorCodes('&', title);
        }
        this.sidebar.setDisplayName((title.length() > 32) ? title.substring(0, 32) : title);
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void setSlot(int slot, String text) {
        Team team = this.scoreboard.getTeam("SLOT_" + slot);
        if(team == null) return;
        String entry = genEntry(slot);
        if (!this.scoreboard.getEntries().contains(entry))
            this.sidebar.getScore(entry).setScore(slot);
        PixelSkScoreboard board = PixelSkScoreboard.getControl();
        if(board.hasPAPI()) {
            text = PlaceholderAPI.setPlaceholders(player,ChatColor.translateAlternateColorCodes('&', text));
        } else {
            text = ChatColor.translateAlternateColorCodes('&', text);
        }
        String pre = getFirstSplit(text);
        String suf = getFirstSplit(ChatColor.getLastColors(pre) + getSecondSplit(text));
        team.setPrefix(pre);
        team.setSuffix(suf);
    }

    public void removeSlot(int slot) {
        String entry = genEntry(slot);
        if (this.scoreboard.getEntries().contains(entry))
            this.scoreboard.resetScores(entry);
    }

    public void updateLines(List<String> list) {
        while (list.size() > 15)
            list.remove(list.size() - 1);
        int slot = list.size();
        if (slot < 15)
            for (int i = slot + 1; i <= 15; i++)
                removeSlot(i);
        PixelSkScoreboard board = PixelSkScoreboard.getControl();
        for (String line : list) {
            if(board.hasPAPI()) {
                line = PlaceholderAPI.setPlaceholders(player,ChatColor.translateAlternateColorCodes('&', line));
            } else {
                line = ChatColor.translateAlternateColorCodes('&', line);
            }
            setSlot(slot, line);
            slot--;
        }
    }

    private String genEntry(int slot) {
        return ChatColor.values()[slot].toString();
    }

    private String getFirstSplit(String s) {
        return (s.length() > 16) ? s.substring(0, 16) : s;
    }

    private String getSecondSplit(String s) {
        if (s.length() > 32)
            s = s.substring(0, 32);
        return (s.length() > 16) ? s.substring(16) : "";
    }
}
