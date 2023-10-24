package org.luvx.coding.guava

import com.google.common.collect.Range
import com.google.common.util.concurrent.RateLimiter
import org.junit.jupiter.api.Test
import org.luvx.coding.common.enhancer.out
import java.time.LocalDateTime

internal class MainTest {

    @Test
    fun m2() {
        val create = RateLimiter.create(1.0)
        LocalDateTime.now().out()
        create.acquire()
        LocalDateTime.now().out()


    }

    @Test
    fun m3() {
        val range = Range.closed(1, 20)
        range.out()
    }
}