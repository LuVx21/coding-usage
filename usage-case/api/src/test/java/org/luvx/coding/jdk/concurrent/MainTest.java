package org.luvx.concurrent;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class MainTest {
    @Test
    void m1() {
        int[] array = IntStream.rangeClosed(0, 100)
                .map(i -> ThreadLocalRandom.current().nextInt(10))
                .toArray();
        System.out.println(Arrays.toString(array));
    }
}
