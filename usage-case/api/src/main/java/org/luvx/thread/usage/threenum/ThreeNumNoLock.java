package org.luvx.thread.usage.threenum;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

/**
 * 三个线程循环打印ABC: 全局计数 实现
 */
@Slf4j
public class ThreeNumNoLock {
    private final Lock lock = new ReentrantLock();

    private volatile int           num0 = 0;
    private          int           num1 = 0;
    private final    AtomicInteger num2 = new AtomicInteger(0);

    public static void main(String[] args) {
        ThreeNumNoLock exec = new ThreeNumNoLock();
        new Thread(() -> exec.run1(0), "t-A").start();
        new Thread(() -> exec.run1(1), "t-B").start();
        new Thread(() -> exec.run1(2), "t-C").start();
    }

    /**
     * state++不保证原子性, 但此处的递增因if 条件变得一个时间只会有一个线程进行操作
     */
    private void run0(int ii) {
        int cnt = 0;
        while (cnt < 10) {
            if (num0 % 3 == ii) {
                log.info("线程:{}, 输出:{}", Thread.currentThread().getName(), (char) (num0 % 3 + 65));
                num0++;
                cnt++;
            }
        }
    }

    /**
     * 此处使用锁是为了确保 num 的递增准确, 如使用AtomicInteger则不用加锁
     */
    public void run1(int i) {
        int cnt = 0;
        while (cnt < 10) {
            try {
                lock.lock();
                while (num1 % 3 == i) {
                    log.info("线程:{}, 输出:{}", Thread.currentThread().getName(), (char) (num1 % 3 + 65));
                    num1++;
                    cnt++;
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private void run2(int i) {
        int cnt = 0;
        while (cnt < 10) {
            if (num2.get() % 3 == i) {
                log.info("线程:{}, 输出:{}", Thread.currentThread().getName(), (char) (num2.get() % 3 + 65));
                num2.incrementAndGet();
                cnt++;
            }
        }
    }


}
