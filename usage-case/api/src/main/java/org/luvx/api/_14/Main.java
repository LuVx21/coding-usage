package org.luvx.api._14;

import io.vavr.API;
import org.luvx.common.util.PrintUtils;

public class Main {
    private record User(Long id, String name, Integer age) {
    }

    // switch yield
    private static String format(Object o) {
        return switch (o) {
            case null -> "null";
            case Integer i -> String.format("int %d", i);
            case Long l -> String.format("long %d", l);
            case Double d -> String.format("double %f", d);
            case String s -> String.format("String %s", s);
            default -> o.toString();
        };
    }

    public static void main(String[] args) {
        User user = new User(1L, "luvx", 18);
        PrintUtils.println(user, format(user));
    }
}
