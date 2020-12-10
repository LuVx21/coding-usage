package org.luvx.kotlin

import kotlin.test.Test

class ATest {
    @Test
    fun testA() {
        val a = A()
        println(
                a.method("world")
        )
    }
}