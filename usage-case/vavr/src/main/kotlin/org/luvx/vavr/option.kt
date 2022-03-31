package org.luvx.vavr

import io.vavr.API
import io.vavr.control.Option
import org.luvx.enhancer.out

private fun m1() {
    val list = listOf(1, 2, 3)
    list
        .find { i -> i < 3 }
        .out()
    val first = list.stream()
        .filter { i -> i > 3 }
        .findFirst()
    Option.ofOptional(first)
        .peek { obj: Int? -> API.println(obj) }
        .getOrElse(-1)
        .out()
}

fun m2() {
    val list = listOf(1, 2, 3, 4, 4)
    Option.of(list)
        .distinct()
        .out()
}

fun main() {
//    m1()
    m2()
}