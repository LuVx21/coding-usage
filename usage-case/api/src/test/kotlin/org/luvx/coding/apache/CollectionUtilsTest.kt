package org.luvx.coding.apache

import org.apache.commons.collections4.CollectionUtils
import org.junit.jupiter.api.Test
import org.luvx.coding.common.enhancer.out

internal class CollectionUtilsTest {
    @Test
    fun m1() {
        val a = listOf("a", "b", "c", "d")
        val b = listOf("d", "e", "f")
        CollectionUtils.union(a, b).out()
//        交集
//        CollectionUtils.intersection
//        补集
        CollectionUtils.disjunction(a, b).out()

//        差集
        CollectionUtils.subtract(a, listOf("b", "c")).out()


    }
}
