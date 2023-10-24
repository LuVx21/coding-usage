package org.luvx.coding.apache

import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.luvx.coding.common.enhancer.out

internal class StringUtilsTest {
    @Test
    fun m1() {
        val s = "abcdefg"
        out(
                StringUtils.leftPad(s, 10, " "),
                StringUtils.substring(s, 0),
                StringUtils.substring(s, 2),
                StringUtils.substring(s, 0, 3)
        )

        Assertions.assertEquals("abc", StringUtils.left(s, 3))
    }

    @Test
    fun m2() {
        RandomStringUtils.random(5).out()
        RandomStringUtils.randomAlphabetic(5).out()
        RandomStringUtils.randomNumeric(5).out()
    }
}