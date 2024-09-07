package org.luvx.coding;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.LongAdder;

@Slf4j
public class Main {
    @Test
    void m1() {
        log.info("┌───────────────────────────────────────────────────┐");
        log.info("│                                                   │");
        log.info("└───────────────────────────────────────────────────┘");
    }

    @Test
    void m2() {
        LongAdder a = new LongAdder();
        a.add(10);
        System.out.println(a);
        a.add(11);
        System.out.println(a);
    }

    @Test
    void m3() {
    }

    @Test
    void m4() {
    }

    @Test
    void m5() {
    }
}
