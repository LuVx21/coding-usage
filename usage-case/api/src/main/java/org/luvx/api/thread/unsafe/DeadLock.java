package org.luvx.api.thread.unsafe;

/**
 * 死锁实现例1
 */
public class DeadLock {
    public static Object objA = new Object();
    public static Object objB = new Object();

    public static void getLockA() {
        synchronized (objA) {
            try {
                Thread.sleep(5 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            getLockB();
        }
    }

    public static void getLockB() {
        synchronized (objB) {
            try {
                Thread.sleep(5 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            getLockA();
        }
    }

    public static void main(String[] args) {
        new Thread(() -> DeadLock.getLockA()).start();
        new Thread(() -> DeadLock.getLockB()).start();
    }
}
