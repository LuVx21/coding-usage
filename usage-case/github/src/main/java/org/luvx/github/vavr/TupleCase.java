package org.luvx.github.vavr;

import io.vavr.Tuple;

public class TupleCase {
    public static void main(String[] args) {
        Tuple.of("Hello", 100)
                .map(String::toLowerCase, v -> v * 5)
                .apply((s, n) -> s + n)
        ;
    }
}
