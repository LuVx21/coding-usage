package org.luvx.collections.list

import com.google.common.collect.Lists
import org.junit.jupiter.api.Test
import org.luvx.enhancer.out
import org.luvx.entity.User
import org.luvx.kotlin.api.entity.User1
import java.util.Map
import java.util.stream.Collectors.*

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

fun m2() {
    val map = Map.of(1, "a", 2, "b", 3, "a")
    listOf(1, 2, 3, 4).stream()
        .filter { o: Int -> map.keys.contains(o) }
        .collect(groupingBy({ key: Int -> map[key] }, toList()))
        .out()
}

@Test
fun m3() {
    val users = Lists.newArrayList<User>()
//    val collect = users.stream().collect(Collectors.toMap(
//        { obj: User -> obj.getId() },
//        { obj: User -> obj.getUserName() },
//        { _: String?, b: String -> b }
//    ))
}