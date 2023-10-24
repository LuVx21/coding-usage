package org.luvx.coding.apache

import org.apache.commons.lang3.Range
import org.junit.jupiter.api.Test
import org.luvx.coding.common.enhancer.out

internal class MainTest {
    @Test
    fun m1() {
        val between = Range.between(1, 20)
        between.isBefore(22).out()
    }
}