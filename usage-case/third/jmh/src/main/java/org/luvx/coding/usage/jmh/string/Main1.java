package org.luvx.coding.usage.jmh.string;

import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Fork(1)
@Threads(8)
@Warmup(iterations = 3)
@Measurement(iterations = 6, time = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Main1 {
    static String s;

    static {
        char[] chars = new char[10_0000];
        Arrays.fill(chars, 'a');
        s = new String(chars);
    }

    @Benchmark
    public void testCharAt() {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
        }
    }

    /**
     * 大约是charAt的3.5
     * <pre>
     * Benchmark         Mode  Cnt    Score    Error   Units
     * Main.testArray   thrpt    6  188.491 ± 16.265  ops/ms
     * Main.testCharAt  thrpt    6   53.080 ±  0.475  ops/ms
     * </pre>
     */
    @Benchmark
    public void testArray() {
        char[] array = s.toCharArray();
        for (int i = 0; i < array.length; i++) {
            char c = array[i];
        }
    }
}
