package org.luvx.kotlin

import org.luvx.java.B
import kotlin.test.Test

private val log = mu.KotlinLogging.logger {}

class ATest {
    @Test
    fun testA() {
        val b = B()
        log.info("in kotlin:{}", b.method("world"))
    }
}