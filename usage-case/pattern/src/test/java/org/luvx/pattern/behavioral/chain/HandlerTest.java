package org.luvx.pattern.behavioral.chain;

import org.junit.Test;

public class HandlerTest {

    @Test
    public void test1() {
        Handler l1 = new Level1Handler();
        Handler l2 = new Level2Handler();
        l1.setNextHandler(l2);

        l1.handle(200);
        l1.handle(2000);
        l1.handle(20000);
    }
}