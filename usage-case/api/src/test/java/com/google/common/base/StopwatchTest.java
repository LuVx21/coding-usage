package com.google.common.base;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class StopwatchTest {

    /**
     * org.springframework.util.StopWatch
     */
    @Test
    void method3() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Thread.sleep(1000);
        long nanos = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println(nanos);
    }
}
