package dev.mruniverse.pixelskscoreboard.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
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
public class CondTitleExistsWithoutName extends Condition {
    Expression<String> file;


    static {
        Skript.registerCondition(CondTitleExistsWithoutName.class, "(scoreboard|skscoreboard) title (in|of) file %string% (1¦is|2¦is(n't| not)) created");
    }

    @SuppressWarnings({"unchecked"})
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.file = (Expression<String>)expressions[0];
        setNegated((parser.mark == 1));
        return true;
    }

    public @NotNull String toString(@Nullable Event event, boolean debug) {
        return "scoreboard title in file " + file.toString(event,debug) + " isn't created";
    }

    @SuppressWarnings("SimplifiableConditionalExpression")
    public boolean check(@NotNull Event event) {
        PixelSkScoreboard board = PixelSkScoreboard.getControl();
        try {
            File configFile = board.getStorage().getExternalFile(Objects.requireNonNull(file.getSingle(event)));
            FileConfiguration configuration = board.getStorage().loadExternalConfigWithFile(configFile);
            return configuration.contains("scoreboard.title") ? isNegated() : (!isNegated());
        }catch (Throwable throwable) {
            board.getLogs().error("Can't check condition, Error generated by an script in this server!");
            board.getLogs().error(throwable);
        }
        return false;
    }
}
