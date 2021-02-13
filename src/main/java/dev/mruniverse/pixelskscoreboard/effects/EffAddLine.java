package dev.mruniverse.pixelskscoreboard.effects;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import dev.mruniverse.pixelskscoreboard.PixelSkScoreboard;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class EffAddLine extends Effect {
    private Expression<String> line;
    private Expression<String> file;
    private Expression<String> name;

    static {
        Skript.registerEffect(EffAddLine.class, "[PixelSkScoreboard |PSS |SkScoreboard ]add line %string% to scoreboard (in|of) file %string% [named %string%]");
    }

    @SuppressWarnings("unchecked")
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        line = (Expression<String>)expressions[0];
        file = (Expression<String>)expressions[1];
        if(expressions[3] != null) {
            name = (Expression<String>) expressions[2];
        } else {
            name = null;
        }
        return true;
    }

    public @NotNull String toString(@Nullable Event event, boolean debug) {
        if(name != null) {
            return "SkScoreboard add line " + line.toString(event, debug) + " to scoreboard in file " + file.toString(event, debug) + " named " + name.toString(event,debug);
        }
        return "SkScoreboard add line " + line.toString(event, debug) + " to scoreboard in file " + file.toString(event, debug);
    }

    protected void execute(@NotNull Event event) {
        if (line == null) return;
        if (file == null) return;
        if (line.getSingle(event) == null) return;
        if (file.getSingle(event) == null) return;
        try {
            String lineToAdd = line.getSingle(event);
            if(lineToAdd == null) lineToAdd = "";
            FileConfiguration configuration = PixelSkScoreboard.getControl().getStorage().loadExternalConfig(Objects.requireNonNull(file.getSingle(event)));
            List<String> lines = new ArrayList<>();
            if(name == null || name.getSingle(event) == null) {
                if(configuration.contains("scoreboard.lines")) {
                    lines = configuration.getStringList("scoreboard.lines");
                }
                lines.add(lineToAdd.replace("§","&"));
                configuration.set("scoreboard.lines",lines);
            } else {
                String path = name.getSingle(event);
                if(path == null) path = "error404";
                if(configuration.contains("scoreboards." + path  + ".lines")) {
                    lines = configuration.getStringList("scoreboards." + path  + ".lines");
                }
                lines.add(lineToAdd.replace("§","&"));
                configuration.set("scoreboards." + path + ".lines",lines);
            }
            PixelSkScoreboard.getControl().getStorage().saveExternal(Objects.requireNonNull(file.getSingle(event)));
        }catch (Throwable throwable) {
            PixelSkScoreboard.getControl().getLogs().error("Can't execute &cEffAddLine.class &7error code: 281");
            PixelSkScoreboard.getControl().getLogs().error(throwable);
        }
    }
}
