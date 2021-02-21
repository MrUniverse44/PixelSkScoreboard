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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class EffAddLineWithoutName extends Effect {
    private Expression<String> line;
    private Expression<String> file;

    static {
        Skript.registerEffect(EffAddLineWithoutName.class, "add [skscoreboard |pixelboard |skboard ]line %string% to scoreboard (in|of) file %string%");
    }

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        line = (Expression<String>)expressions[0];
        file = (Expression<String>)expressions[1];
        return true;
    }

    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "add skboard line " + line.toString(event, debug) + " to scoreboard in file " + file.toString(event, debug);
    }

    protected void execute(@NotNull Event event) {
        if (line == null) return;
        if (file == null) return;
        if (line.getSingle(event) == null) return;
        if (file.getSingle(event) == null) return;
        PixelSkScoreboard board = PixelSkScoreboard.getControl();
        try {
            String lineToAdd = line.getSingle(event);
            if(lineToAdd == null) lineToAdd = "";
            File configFile = board.getStorage().getExternalFile(Objects.requireNonNull(file.getSingle(event)));
            FileConfiguration configuration = board.getStorage().loadExternalConfigWithFile(configFile);
            List<String> lines = new ArrayList<>();
            if(configuration.contains("scoreboard.lines")) {
                lines = configuration.getStringList("scoreboard.lines");
            }
            lines.add(lineToAdd.replace("§","&"));
            configuration.set("scoreboard.lines",lines);
            configuration.save(configFile);
        }catch (Throwable throwable) {
            board.getLogs().error("Can't execute &cEffAddLineWithoutName.class &7error code: 281 &8(Probably is an issue created in your script)");
            board.getLogs().error(throwable);
        }
    }
}
