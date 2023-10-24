package org.luvx.coding.jdk.concurrent.usage.threenum;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 三个线程循环打印ABC: 全局计数+线程通信
 */
@Slf4j
public class ThreeNum2 {
    private static final Lock lock = new ReentrantLock();
    private              int  num  = 0;

    public static void main(String[] args) {
        // final Object a = new Object(), b = new Object(), c = new Object();
        final Condition a = lock.newCondition(), b = lock.newCondition(), c = lock.newCondition();
        ThreeNum2 exec = new ThreeNum2();
        new Thread(() -> exec.run(a, b, 'A'), "t-A").start();
        new Thread(() -> exec.run(b, c, 'B'), "t-B").start();
        new Thread(() -> exec.run(c, a, 'C'), "t-C").start();
    }

    @SneakyThrows
    public void run(Object cur, Object next, char ch) {

    }

    /**
     * 实现1
     */
    @SneakyThrows
    public void run(Condition cur, Condition next, char ch) {
        try {
            lock.lock();
            for (int i = 0; i < 10; i++) {
                log.info("线程:{}, 输出:{}", Thread.currentThread().getName(), ch);
                next.signal();
                cur.await();
            }
            next.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 实现2: 同时使用Condition 和全局变量 两个通信方式
     */
    @SneakyThrows
    public void run(Condition cur, Condition next, int ii) {
        for (int i = 0; i < 10; i++) {
            try {
                lock.lock();
                while (num % 3 != ii) {
                    cur.await();
                }
                info();
                next.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 实现3: 类似于 2
     */
    @SneakyThrows
    public void run(Object cur, Object next, int ii) {
        for (int i = 0; i < 10; i++) {
            synchronized (cur) {
                while (num % 3 != ii) {
                    cur.wait();
                }
                info();
                cur.notify();
            }
            synchronized (next) {
                next.notify();
            }
        }
    }

    private void info() {
        log.info("线程:{}, 输出:{}", Thread.currentThread().getName(), (char) (num % 3 + 65));
        num++;
    }
}
