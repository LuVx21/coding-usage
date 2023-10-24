package org.luvx.coding.jdk.concurrent.notify.pc;

import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Case01 t = new Case01();
        new Thread(t.new Producer()).start();
        new Thread(t.new Producer()).start();
        new Thread(t.new Producer()).start();
        new Thread(t.new Producer()).start();

        TimeUnit.SECONDS.sleep(3);

        new Thread(t.new Consumer()).start();
        // new Thread(t.new Consumer()).start();
        // new Thread(t.new Consumer()).start();
        // new Thread(t.new Consumer()).start();
    }
}
