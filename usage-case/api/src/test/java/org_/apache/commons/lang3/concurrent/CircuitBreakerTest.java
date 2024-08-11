package org_.apache.commons.lang3.concurrent;

import org.apache.commons.lang3.concurrent.ThresholdCircuitBreaker;
import org.junit.jupiter.api.Test;

class CircuitBreakerTest {
    @Test
    void thresholdCircuitBreakerTest() {
        // EventCountCircuitBreaker eventCountCircuitBreaker = new EventCountCircuitBreaker();
        ThresholdCircuitBreaker breaker = new ThresholdCircuitBreaker(10L);

        if (breaker.incrementAndCheckState(9L)) {
            System.out.println("处理请求");
            breaker.close();
        } else {
            System.out.println("请求被拒绝");
        }
    }
}
