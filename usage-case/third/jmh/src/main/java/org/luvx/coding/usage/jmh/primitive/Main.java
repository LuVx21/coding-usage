package org.luvx.coding.usage.jmh.primitive;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Threads(8)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class Main {
    @Benchmark
    public void m0() {
        Long sum = 0L;
        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            sum += i;
        }
        // System.out.println(sum);
    }

    @Benchmark
    public void m1() {
        long sum = 0L;
        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            sum += i;
        }
        // System.out.println(sum);
    }
}
