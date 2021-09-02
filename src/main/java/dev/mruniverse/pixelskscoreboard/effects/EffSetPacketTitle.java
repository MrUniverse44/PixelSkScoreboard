package dev.mruniverse.pixelskscoreboard.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.mruniverse.pixelskscoreboard.PixelSkScoreboard;
import dev.mruniverse.pixelskscoreboard.netherboard.BPlayerBoard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


@SuppressWarnings("unused")
public class EffSetPacketTitle extends Effect {
    private Expression<String> title;
    private Expression<Player> player;

    static {
        Skript.registerEffect(EffSetPacketTitle.class, "set (skscoreboard|pixelSk|pixel) title of [current ]scoreboard of %players% to %string% (with|using) packets");
    }

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        player = (Expression<Player>)expressions[0];
        title = (Expression<String>)expressions[1];
        return true;
    }

    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "set (skscoreboard|pixelSk|pixel) title of [current ]scoreboard of " + player.toString(event, debug) + " to " + title.toString(event,debug) + " using packets";
    }

    protected void execute(@NotNull Event event) {
        if (title == null) return;
        if (player == null) return;
        if (title.getSingle(event) == null) return;
        PixelSkScoreboard board = PixelSkScoreboard.getControl();
        try {
            String titleToSet = title.getSingle(event);
            if(titleToSet == null) titleToSet = "";
            titleToSet = ChatColor.translateAlternateColorCodes('&',titleToSet);
            for(Player player : this.player.getAll(event)) {
                BPlayerBoard manager = board.getPacketScoreboards().getToAdd(player);
                manager.setName(titleToSet);
            }

        }catch (Throwable throwable) {
            board.getLogs().error("Can't execute &cEffSetPacketTitle.class &7error code: 281 &8(Probably is an issue created in your script)");
            board.getLogs().error(throwable);
        }
    }
}
