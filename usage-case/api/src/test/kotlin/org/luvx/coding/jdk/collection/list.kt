package org.luvx.coding.jdk.collection

import com.google.common.collect.Lists
import org.junit.jupiter.api.Test
import org.luvx.coding.common.enhancer.out
import org.luvx.kotlin.api.entity.User1
import java.util.*
import java.util.Map
import java.util.function.BiFunction
import java.util.stream.Collectors
import java.util.stream.Collectors.*


internal class Main {
    fun a1() {
        val list = Lists.newArrayList(1, 2, 3, 4, 5)
        val iterator = list.spliterator()

        var current = 0
        while (iterator.tryAdvance { a -> (a + 1).out() }) {
            current++
        }
    }

    fun m1() {
        listOf(
                User1(1, 18), User1(2, 19),
                User1(3, 19), User1(4, 18)
        ).stream()
                // .collect(groupingBy { u -> u.age })
                .collect(groupingBy({ u -> u.age }, mapping({ u -> u.id }, toList())))
                .out()
    }


    @Test
    fun m2() {
        val map = Map.of(1, "a", 2, "b", 3, "a")
        val list = listOf(1, 2, 3, 4)
        list.stream()
                .filter { o: Int -> map.keys.contains(o) }
                .collect(groupingBy({ key: Int -> map[key] }, toList()))
                .out()

        list.stream()
                .collect(Collectors.partitioningBy() { n -> n % 2 == 0 })
                .out()
    }

    @Test
    fun m3() {
        val list = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i")
        val collect = list.stream()
                .collect(teeing(
                        minBy(Comparator.naturalOrder()),
                        maxBy(Comparator.naturalOrder())
                ) { min, max -> min.get() + "~" + max.get() })
        println(collect)
    }
}