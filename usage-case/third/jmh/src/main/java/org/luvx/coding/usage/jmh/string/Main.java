package org.luvx.coding.usage.jmh.string;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Threads(8)
@Warmup(iterations = 3)
@Measurement(iterations = 6, time = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Main {

    @Benchmark
    public void testStringAdd() {
        String a = "";
        for (int i = 0; i < 10; i++) {
            a += i;
        }
        print(a);
    }

    @Benchmark
    public void testStringBuilderAdd() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(i);
        }
        print(sb.toString());
    }

    private void print(String a) {
    }
}