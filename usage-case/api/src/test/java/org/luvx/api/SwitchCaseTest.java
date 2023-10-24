package org.luvx.api;

import org.junit.jupiter.api.Test;

public class SwitchCaseTest {

    // switch yield
    private String format(Object o) {
        return switch (o) {
            case null -> "null";
            case Integer i -> String.format("int %d", i);
            case Long l -> String.format("long %d", l);
            case Double d -> String.format("double %f", d);
            case String s -> String.format("String %s", s);
            default -> o.toString();
        };
    }

    void testStringEnhanced(String response) {
        switch (response) {
            case null -> {
            }
            case "y", "Y" -> System.out.println("You got it");
            case "n", "N" -> System.out.println("Shame");
            case String s
                    when s.equalsIgnoreCase("YES") -> {
                System.out.println("You got it");
            }
            case String s
                    when s.equalsIgnoreCase("NO") -> {
                System.out.println("Shame");
            }
            case String s -> System.out.println("Sorry?");
        }
    }

    @Test
    void m1() {
        System.out.println(format("字符串"));
        System.out.println(format(1));
        System.out.println(format(8.88D));
    }
}
