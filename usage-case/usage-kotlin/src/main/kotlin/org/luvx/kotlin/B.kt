package org.luvx.kotlin

import org.luvx.java.B

private val log = mu.KotlinLogging.logger {}

fun demo(source: List<Int>) {
    val list = ArrayList<Int>()

    for (item in source) {
        list.add(item)
    }
    println(list)

    for (i in source.indices) {
        list[source.size - 1 - i] = source[i]
    }
    println(list)
}

fun main() {
    val b = B();
    val s = b.method("world")
    log.info { "in kotlin: $s" }

    val list = listOf<Int>(1, 2, 3, 4, 5)
    demo(list)
}