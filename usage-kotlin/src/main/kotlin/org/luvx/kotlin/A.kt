package org.luvx.kotlin

class A {
    private val log = mu.KotlinLogging.logger {}

    fun method(s: String): String {
        return "hello $s from kotlin!"
    }
}
