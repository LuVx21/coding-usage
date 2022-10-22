package org.luvx.coding.usage.jmh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.math3.util.Pair;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import lombok.AllArgsConstructor;

@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Main2 {
    static List<Pair<Integer, String>> demoList;

    static {
        demoList = new ArrayList<>();
        for (int i = 0; i < 10_0000; i++) {
            demoList.add(new Pair<>(i, "test"));
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

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(Main2.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}