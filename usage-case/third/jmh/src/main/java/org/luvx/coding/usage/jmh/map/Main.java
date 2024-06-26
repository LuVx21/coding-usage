package org.luvx.coding.usage.jmh.map;

import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Main {
    static List<Pair<Integer, String>> demoList;

    static {
        demoList = new ArrayList<>();
        for (int i = 0; i < 10_0000; i++) {
            demoList.add(Pair.of(i, "test"));
        }
    }

    @Benchmark
    public void testHashMapWithoutSize() {
        Map<Integer, String> map = new HashMap<>();
        for (Pair<Integer, String> pair : demoList) {
            map.put(pair.getKey(), pair.getValue());
        }
    }

    @Benchmark
    public void testHashMapWithSize() {
        Map<Integer, String> map = new HashMap<>((int) (demoList.size() / 0.75f) + 1);
        for (Pair<Integer, String> pair : demoList) {
            map.put(pair.getKey(), pair.getValue());
        }
    }
}