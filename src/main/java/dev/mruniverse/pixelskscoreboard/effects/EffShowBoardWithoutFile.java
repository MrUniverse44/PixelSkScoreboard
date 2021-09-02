package dev.mruniverse.pixelskscoreboard.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.mruniverse.pixelskscoreboard.PixelSkScoreboard;
import dev.mruniverse.pixelskscoreboard.utils.scoreboards.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class EffShowBoardWithoutFile extends Effect {
    private Expression<Player> player;

    static {
        Skript.registerEffect(EffShowBoardWithoutFile.class, "(send|show) [skscoreboard |pixelboard |skboard ]scoreboard (to|for) %players%");
    }

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        player = (Expression<Player>)expressions[0];
        return true;
    }

    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "send pixelboard scoreboard for " + player.toString(event,debug);
    }

    protected void execute(@NotNull Event event) {
        if (player == null) return;
        PixelSkScoreboard board = PixelSkScoreboard.getControl();
        try {
            board.getLogs().info("show skscoreboard for [player(s)] | is in maintenance.");
        }catch (Throwable throwable) {
            board.getLogs().error("Can't execute &cEffShowBoard.class &7error code: 281 &8(Probably is an issue created in your script)");
            board.getLogs().error(throwable);
        }
    }
}
