package org.luvx.utils;

import java.util.Objects;
import java.util.function.BiConsumer;

public class Equals {
    public static <T, U> void equalsThen(T t, U u, BiConsumer<T, U> consumer) {
        if (Objects.equals(t, u)) {
            consumer.accept(t, u);
        }
    }
}
