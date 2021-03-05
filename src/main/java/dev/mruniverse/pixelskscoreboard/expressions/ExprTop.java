package dev.mruniverse.pixelskscoreboard.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
@SuppressWarnings("unused")
public class ExprTop extends PropertyExpression<String,String> {
    private Expression<Integer> number;
    private Expression<Map<Object,Object>> objects;
    private Expression<String> format;

    static {
        Skript.registerExpression(ExprTop.class, String.class, ExpressionType.COMBINED,
                "top %integer% values of %objects% formatted as %string%");
    }

    @Override
    public @NotNull Class<? extends String> getReturnType() {
        //1
        return String.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?> @NotNull [] expressions, int matchedPattern, @NotNull Kleenean isDelayed, SkriptParser.@NotNull ParseResult parser) {
        number = (Expression<Integer>)expressions[2];
        objects = (Expression<Map<Object,Object>>)expressions[0];
        format = (Expression<String>) expressions[1];
        return true;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean debug) {
        //4
        return "top " + number.toString(event,debug) + " values of " + objects.toString(event,debug) + " formatted as " + format.toString(event,debug);
    }

    @Override
    protected String @NotNull [] get(@NotNull Event event, String @NotNull [] source) {
        try {
            int line = 1;
            Map<Object,Object> check = objects.getSingle(event);
            if(check == null) return new String[0];
            String[] returnal = new String[check.entrySet().size() + 1];
            for (Map.Entry<Object,Object> entry : check.entrySet()) {
                String lina = format.getSingle(event);
                if(lina == null) lina = "<position> <index> <value>";
                returnal[line] = lina.replace("<position>",line + "")
                .replace("<index>",entry.getKey().toString())
                .replace("<value>",entry.getValue().toString());
                line++;
            }
            return returnal;
        } catch (Throwable ignored) {}
        return new String[0];
    }
}