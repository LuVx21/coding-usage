package org.luvx.java;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.luvx.kotlin.A;

@Slf4j
public class BTest {
    @Test
    public void bTest() {
        A a = new A();
        log.info("in java:{}", a.method("world"));
    }
}