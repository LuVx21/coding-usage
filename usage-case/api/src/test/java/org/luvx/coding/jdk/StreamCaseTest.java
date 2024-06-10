package org.luvx.coding.jdk;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class StreamCaseTest {
    @Test
    void m0() {
        Stream.iterate(0, n -> n + 2)
                .limit(10)
                .forEach(System.out::println);
    }

    @Test
    void m1() {
        List<String> fruits = Arrays.asList("apple", "banana", "orange");
        Map<Integer, String> result = fruits.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(fruits::indexOf, String::toUpperCase),
                        Collections::unmodifiableMap
                ));
        System.out.println(result);
    }

    @Test
    void m2() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7);
        numbers.stream()
                .dropWhile(n -> n < 3)
                .takeWhile(n -> n < 6)
                .forEach(System.out::println);
    }

    @Test
    void m3() {
        Map<String, Integer> collect = Stream.of(1, 2, 3, 4)
                .collect(Collectors.teeing(
                        Collectors.maxBy(Integer::compareTo),
                        Collectors.minBy(Integer::compareTo),
                        (e1, e2) -> Map.of("max", e1.get(), "min", e2.get())
                ));

        System.out.println(collect);
    }
}
