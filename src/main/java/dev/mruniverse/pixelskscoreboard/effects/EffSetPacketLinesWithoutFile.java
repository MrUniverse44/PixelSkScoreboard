package dev.mruniverse.pixelskscoreboard.effects;


import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.mruniverse.pixelskscoreboard.PixelSkScoreboard;
import dev.mruniverse.pixelskscoreboard.netherboard.BPlayerBoard;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class EffSetPacketLinesWithoutFile extends Effect {
    private Expression<String> lines;
    private Expression<Player> player;

    static {
        Skript.registerEffect(EffSetPacketLinesWithoutFile.class, "set (skscoreboard|pixelSk|pixel) lines of [current ]scoreboard of %players% to %strings% (with|using) packets");
    }

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        player = (Expression<Player>)expressions[0];
        lines = (Expression<String>)expressions[1];
        return true;
    }

    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "set (skscoreboard|pixelSk|pixel) lines of [current ]scoreboard of " + player.toString(event, debug) + " to " + lines.toString(event,debug) + " with packets";
    }

    protected void execute(@NotNull Event event) {
        if (lines == null) return;
        if (player == null) return;
        if (lines.getSingle(event) == null) return;
        PixelSkScoreboard board = PixelSkScoreboard.getControl();
        try {
            List<String> lines = new ArrayList<>(Arrays.asList(this.lines.getArray(event)));
            String[] array = new String[lines.size()];
            lines.toArray(array);
            for (Player player : this.player.getAll(event)) {
                BPlayerBoard manager = board.getPacketScoreboards().getToAdd(player);
                manager.setAll(array);
            }

        }catch (Throwable throwable) {
            board.getLogs().error("Can't execute &cEffSetPacketLinesWithoutFile.class &7error code: 281 &8(Probably is an issue created in your script)");
            board.getLogs().error(throwable);
        }
    }
}
