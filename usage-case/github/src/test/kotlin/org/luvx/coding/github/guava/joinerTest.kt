package org.luvx.coding.github.guava

import com.google.common.base.Joiner
import com.google.common.collect.ImmutableMap
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsSame
import org.junit.jupiter.api.Test
import org.luvx.coding.common.enhancer.out
import org.luvx.coding.common.more.MorePrints
import java.util.stream.Collectors

internal class JoinerTest {

    private val stringList: List<String> = listOf(
        "Google", "Guava", "Java", "Scala", "Kafka"
    )
    private val stringListWithNullValue: List<String?> = mutableListOf(
        "Google", "Guava", "Java", "Scala", null
    )
    private val stringMap: Map<String, String> = ImmutableMap.of("Hello", "Guava", "Java", "Scala")

    @Test
    fun m1() {
        Joiner.on("&")
            .join("a", "b", "c", "d", 1)
            .out()

        Joiner.on("&")
            .withKeyValueSeparator("=")
            .join(ImmutableMap.of("id", 1, "name", "java"))
            .out()
    }

    @Test
    fun testJoinOnJoin() {
        val result = Joiner
            .on("#")
            .join(stringList)
        MatcherAssert.assertThat(result, IsEqual.equalTo("Google#Guava#Java#Scala#Kafka"))
    }

    @Test
    fun testJoin_On_Append_To_Writer() {
        var sb = Joiner.on("#").useForNull("DEFAULT").appendTo(StringBuilder(), stringListWithNullValue)
        MorePrints.println(sb)
        sb = Joiner.on("#").withKeyValueSeparator("=").appendTo(StringBuilder(), stringMap)
        MorePrints.println(sb)
    }

    @Test
    fun testJoinOnJoinWithNullValueButSkip() {
        val result = Joiner.on("#")
            .skipNulls()
            .join(stringListWithNullValue)
        MatcherAssert.assertThat(result, IsEqual.equalTo("Google#Guava#Java#Scala"))
    }

    @Test
    fun testJoin_On_Join_WithNullValue_UseDefaultValue() {
        val result = Joiner.on("#").useForNull("DEFAULT").join(stringListWithNullValue)
        MatcherAssert.assertThat(result, IsEqual.equalTo("Google#Guava#Java#Scala#DEFAULT"))
    }

    @Test
    fun testJoin_On_Append_To_StringBuilder() {
        val builder = StringBuilder()
        val resultBuilder = Joiner.on("#").useForNull("DEFAULT").appendTo(builder, stringListWithNullValue)
        MatcherAssert.assertThat(resultBuilder, IsSame.sameInstance(builder))
        MatcherAssert.assertThat(resultBuilder.toString(), IsEqual.equalTo("Google#Guava#Java#Scala#DEFAULT"))
        MatcherAssert.assertThat(builder.toString(), IsEqual.equalTo("Google#Guava#Java#Scala#DEFAULT"))
    }

    @Test
    fun testJoiningByStreamSkipNullValues() {
        val result = stringListWithNullValue.stream().filter { item: String? -> item != null && !item.isEmpty() }
            .collect(Collectors.joining("#"))
        MatcherAssert.assertThat(result, IsEqual.equalTo("Google#Guava#Java#Scala"))
    }

    @Test
    fun testJoiningByStreamWithDefaultValue() {
        val result = stringListWithNullValue.stream()
            .map { s -> s ?: "DEFAULT" }
            .collect(Collectors.joining("#"))
        MatcherAssert.assertThat(result, IsEqual.equalTo("Google#Guava#Java#Scala#DEFAULT"))
    }

    @Test
    fun testJoinOnWithMap() {
        MatcherAssert.assertThat(
            Joiner.on('#').withKeyValueSeparator("=").join(stringMap),
            IsEqual.equalTo("Hello=Guava#Java=Scala")
        )
    }
}