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


@SuppressWarnings("unused")
public class EffSetPacketSlot extends Effect {
    private Expression<String> lines;
    private Expression<Number> slot;
    private Expression<Player> player;

    static {
        Skript.registerEffect(EffSetPacketSlot.class, "set (skscoreboard|pixelSk|pixel) slot %-number% of [current ]scoreboard of %players% to %string% (with|using) packets");
    }

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        player = (Expression<Player>)expressions[1];
        lines = (Expression<String>)expressions[2];
        slot = (Expression<Number>)expressions[0];
        return true;
    }

    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "set (skscoreboard|pixelSk|pixel) slot " + slot.toString(event,debug) + " of [current ]scoreboard of " + player.toString(event, debug) + " to " + lines.toString(event,debug) + " with packets";
    }

    protected void execute(@NotNull Event event) {
        if (lines == null || player == null || slot == null) return;
        if (lines.getSingle(event) == null) return;
        if (slot.getSingle(event) == null) return;
        PixelSkScoreboard board = PixelSkScoreboard.getControl();
        try {
            String lineToSet = lines.getSingle(event);
            if(lineToSet == null) lineToSet = "";
            final Number n = this.slot.getSingle(event);
            if(n == null) return;
            int slot = n.intValue();
            for(Player player : this.player.getAll(event)) {
                BPlayerBoard manager = board.getPacketScoreboards().getToAdd(player);
                manager.set(lineToSet, slot);
            }
        }catch (Throwable throwable) {
            board.getLogs().error("Can't execute &cEffSetPacketSlot.class &7error code: 281 &8(Probably is an issue created in your script)");
            board.getLogs().error(throwable);
        }
    }
}

