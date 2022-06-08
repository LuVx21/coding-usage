package org.luvx.concurrent.usage;

import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;

/**
 * 死锁实现例1
 */
public class DeadLock {
    public static final Object objA = new Object();
    public static final Object objB = new Object();

    @SneakyThrows
    public static void getLockA() {
        synchronized (objA) {
            TimeUnit.SECONDS.sleep(5);
            getLockB();
        }
    }

    @SneakyThrows
    public static void getLockB() {
        synchronized (objB) {
            TimeUnit.SECONDS.sleep(5);
            getLockA();
        }
    }

    public static void main(String[] args) {
        new Thread(DeadLock::getLockA).start();
        new Thread(DeadLock::getLockB).start();
    }
}
