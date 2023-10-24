package org.luvx.concurrent.usage.counter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName: org.luvx.api.thread.usage
 * @Description: 不安全的计数器及解决方案
 * @Author: Ren, Xie
 * @Date: 2019/7/31 15:22
 */
public class IncreaseCase {
    private        int           num  = 0;
    private static ReentrantLock lock = new ReentrantLock();
    private        AtomicInteger num1 = new AtomicInteger(0);

    /**
     * 不安全的自增方式
     */
    public void run() {
        final CountDownLatch count = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    num++;
                }
                count.countDown();
            }).start();
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.num);
    }

    /**
     * 安全的方式1
     */
    public void run1() {
        final CountDownLatch count = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    lock.lock();
                    try {
                        num++;
                    } finally {
                        lock.unlock();
                    }
                    // synchronized (Counter.class) {
                    //     num++;
                    // }
                }
                count.countDown();
            }).start();
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.num);
    }

    /**
     * 安全的方式2
     */
    public void run2() {
        final CountDownLatch count = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    num1.getAndIncrement();
                }
                count.countDown();
            }).start();
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.num1);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new IncreaseCase().run();
        }
    }
}
