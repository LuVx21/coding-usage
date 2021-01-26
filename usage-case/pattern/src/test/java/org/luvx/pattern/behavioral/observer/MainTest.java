package org.luvx.pattern.behavioral.observer;

import org.junit.Test;

/**
 * @Author: Ren, Xie
 * @Date: 2019/7/9 9:52
 */
public class MainTest {
    @Test
    public void mainTest() {
        Light light = new Light();
        light.attach(new Driver());
        light.attach(new Driver());
        light.attach(new Driver());
        light.attach(new Driver());
        light.notifyObserver();
    }
}
