package org.luvx.github.guava.common

import com.google.common.base.Joiner
import com.google.common.collect.ImmutableMap
import org.junit.jupiter.api.Test
import org.luvx.coding.common.enhancer.out

internal class Main {
    @Test
    fun m1() {
        Joiner.on("&")
            .join("a", "b", "c", "d", 1)
            .out()

        Joiner.on("&")
            .withKeyValueSeparator("=")
            .join(ImmutableMap.of("id", 1, "name", "java"))
            .out()
    }
}