package dev.mruniverse.pixelskscoreboard.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.mruniverse.pixelskscoreboard.PixelSkScoreboard;
import dev.mruniverse.pixelskscoreboard.utils.scoreboards.PlayerManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;
import java.util.Objects;
@SuppressWarnings("unused")
public class EffShowBoardWithoutName extends Effect {
    private Expression<Player> player;
    private Expression<String> file;

    static {
        Skript.registerEffect(EffShowBoardWithoutName.class, "(send|show) [skscoreboard |pixelboard |skboard ]scoreboard (in|of) file %string% (to|for) %players%");
    }

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        player = (Expression<Player>)expressions[1];
        file = (Expression<String>)expressions[0];
        return true;
    }

    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "send pixelboard scoreboard of file " + file.toString(event,debug) + " for " + player.toString(event,debug);
    }

    protected void execute(@NotNull Event event) {
        if (player == null) return;
        if (file == null) return;
        if (file.getSingle(event) == null) return;
        PixelSkScoreboard board = PixelSkScoreboard.getControl();
        try {
            File configFile = board.getStorage().getExternalFile(Objects.requireNonNull(file.getSingle(event)));
            FileConfiguration configuration = board.getStorage().loadExternalConfigWithFile(configFile);
            if(configFile == null) return;
            if(configuration == null) return;
            if(!configuration.contains("scoreboard.lines")) return;
            if(!configuration.contains("scoreboard.title")) return;
            String title = configuration.getString("scoreboard.title");
            List<String> lines = configuration.getStringList("scoreboard.lines");
            for(Player p : player.getAll(event)) {
                PlayerManager manager = board.getScoreboards().getToAdd(p);
                manager.setTitle(title);
                manager.updateLines(lines);
            }
        }catch (Throwable throwable) {
            board.getLogs().error("Can't execute &cEffShowBoard.class &7error code: 281 &8(Probably is an issue created in your script)");
            board.getLogs().error(throwable);
        }
    }
}
