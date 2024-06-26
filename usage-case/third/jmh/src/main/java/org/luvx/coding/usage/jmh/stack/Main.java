package org.luvx.coding.usage.jmh.stack;

import org.openjdk.jmh.annotations.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

/**
 * Stack使用了synchronized, 慢
 * 单线程场景下, Deque比Stack只快了一点点(Deque受限于数组的扩容)
 */
@Fork(1)
@Threads(8)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 6, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class Main {
    final int cnt = 100_0000;

    @Benchmark
    public void testStackPush() {
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < cnt; i++) {
            stack.push(i);
        }
        int size = stack.size();
        while (!stack.isEmpty()) {
            stack.pop();
        }
    }

    @Benchmark
    public void testDequePush() {
        Deque<Integer> stack = new ArrayDeque<>(cnt);
        for (int i = 0; i < cnt; i++) {
            stack.push(i);
        }
        int size = stack.size();
        while (!stack.isEmpty()) {
            stack.pop();
        }
    }
}