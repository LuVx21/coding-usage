package org.luvx.vavr

import io.vavr.Tuple
import org.luvx.enhancer.out

fun main() {
    Tuple.of("Hello", 100)
        .map({ obj -> obj.lowercase() }) { v -> v * 5 }
        .apply { s, n -> s + n }
        .out()
}