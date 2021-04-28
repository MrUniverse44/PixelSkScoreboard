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
public class EffSetTitleWithoutFile extends Effect {
    private Expression<String> title;
    private Expression<Player> player;

    static {
        Skript.registerEffect(EffSetTitleWithoutFile.class, "set (skscoreboard|pixelSk|pixel) title of [current ]scoreboard of %players% to %string%");
    }

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        player = (Expression<Player>)expressions[0];
        title = (Expression<String>)expressions[1];
        return true;
    }

    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "set (skscoreboard|pixelSk|pixel) title of [current ]scoreboard of " + player.toString(event, debug) + " to " + title.toString(event,debug);
    }

    protected void execute(@NotNull Event event) {
        if (title == null) return;
        if (player == null) return;
        if (title.getSingle(event) == null) return;
        if (player.getSingle(event) == null) return;
        PixelSkScoreboard board = PixelSkScoreboard.getControl();
        try {
            String titleToSet = title.getSingle(event);
            if(titleToSet == null) titleToSet = "";
            for (Player player : this.player.getAll(event)) {
                PlayerManager manager = board.getScoreboards().getToAdd(player);
                manager.setTitle(titleToSet);
            }

        }catch (Throwable throwable) {
            board.getLogs().error("Can't execute &cEffSetTitleWithoutFile.class &7error code: 281 &8(Probably is an issue created in your script)");
            board.getLogs().error(throwable);
        }
    }
}
