package com.google.common.math;

import org.junit.jupiter.api.Test;

class LongMathTest {
    @Test
    void m1() {
        long l = LongMath.saturatedAdd(100, Long.MAX_VALUE);
        System.out.println(Long.MAX_VALUE);
        System.out.println(l);
    }
}
