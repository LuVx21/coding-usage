package org.luvx.coding.jdk.collections.map;

import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Maps;

public class MapCase {

    public static void main(String[] args) {
        // m1();
        m2();
    }

    private static void m2() {
        Map<Long, Integer> map = Maps.newHashMap();
        for (int i = 0; i < 3; i++) {
            map.compute(100L, (k, v) -> v == null ? 1 : ++v);
            // map.computeIfAbsent(100L, k -> 0);
            // map.computeIfPresent(100L, (k, v) ->  );
            System.out.println(map);
        }
        // System.out.println(map.putIfAbsent(100L, 1));
    }

    private static void m1() {
        Map<String, List<Integer>> map = Map.of(
                "a", List.of(0, 1, 2, 3),
                "b", List.of(0, 4, 5, 6)
        );
        // 1
        Map<Integer, String> map1 = Maps.newHashMap();
        map.forEach((k, v) -> v.forEach(i -> map1.put(i, k)));
        System.out.println(map1);

        // 2
        Map<Integer, String> map2 = map.entrySet().stream()
                .flatMap(e -> e.getValue().stream().map(i -> Pair.of(i, e.getKey())))
                .collect(
                        // 保留所有
                        // groupingBy(Pair::getKey, mapping(Pair::getValue, toList()))
                        // 保留一个
                        toMap(Pair::getKey, Pair::getValue, (o, n) -> n)
                );
        System.out.println(map2);
    }
}
