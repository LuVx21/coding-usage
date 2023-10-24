package org.luvx.coding.jdk.lang

import mu.KotlinLogging
import org.junit.jupiter.api.Test

private val log = KotlinLogging.logger {}

internal class Main {
    @Test
    fun m1() {
        log.info { "aaa" }
    }
}