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
public class EffHidePacketBoard extends Effect {
    private Expression<Player> player;

    static {
        Skript.registerEffect(EffHidePacketBoard.class, "(hide|delete) [skscoreboard |pixelboard |skboard ]scoreboard (of|for) %players% (with|using) packets");
    }

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        player = (Expression<Player>)expressions[0];
        return true;
    }

    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "hide pixelboard scoreboard of " + player.toString(event,debug) + " using packets";
    }

    protected void execute(@NotNull Event event) {
        if (player == null) return;
        PixelSkScoreboard board = PixelSkScoreboard.getControl();
        try {
            for(Player p : player.getAll(event)) {
                BPlayerBoard manager = board.getPacketScoreboards().getToAdd(p);
                manager.delete();
            }
        }catch (Throwable throwable) {
            board.getLogs().error("Can't execute &cEffHidePacketBoard.class &7error code: 281 &8(Probably is an issue created in your script)");
            board.getLogs().error(throwable);
        }
    }
}