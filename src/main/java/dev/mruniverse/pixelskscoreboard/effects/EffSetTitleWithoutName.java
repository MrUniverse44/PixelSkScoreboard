package dev.mruniverse.pixelskscoreboard.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.mruniverse.pixelskscoreboard.PixelSkScoreboard;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Objects;

@SuppressWarnings("unused")
public class EffSetTitleWithoutName  extends Effect {
    private Expression<String> title;
    private Expression<String> file;

    static {
        Skript.registerEffect(EffSetTitleWithoutName.class, "set [skscoreboard |pixelboard |skboard ]title %string% of scoreboard (in|of) file %string%");
    }

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        title = (Expression<String>) expressions[0];
        file = (Expression<String>) expressions[1];
        return true;
    }

    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "set title " + title.toString(event, debug) + " of scoreboard in file " + file.toString(event, debug);
    }

    protected void execute(@NotNull Event event) {
        if (title == null) return;
        if (file == null) return;
        if (title.getSingle(event) == null) return;
        if (file.getSingle(event) == null) return;
        PixelSkScoreboard board = PixelSkScoreboard.getControl();
        try {
            String titleToSet = title.getSingle(event);
            if (titleToSet == null) titleToSet = "";
            File configFile = board.getStorage().getExternalFile(Objects.requireNonNull(file.getSingle(event)));
            FileConfiguration configuration = board.getStorage().loadExternalConfigWithFile(configFile);
            if (configFile == null) return;
            if (configuration == null) return;
            configuration.set("scoreboard.title", titleToSet);
            configuration.save(configFile);
            board.getLogs().debug("Title: '" + titleToSet + "&7' added to scoreboard in '" + file.getSingle(event) + "'");
        } catch (Throwable throwable) {
            board.getLogs().error("Can't execute &cEffSetTitleWithoutName.class &7error code: 281 &8(Probably is an issue created in your script)");
            board.getLogs().error(throwable);
        }
    }
}
