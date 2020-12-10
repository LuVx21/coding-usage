package org.luvx.java;

import org.junit.Test;

public class BTest {
    @Test
    public void bTest() {
        B b = new B();
        System.out.println(
                b.method("world")
        );
    }
}