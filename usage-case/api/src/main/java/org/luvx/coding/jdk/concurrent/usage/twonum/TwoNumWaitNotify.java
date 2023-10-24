package org.luvx.concurrent.usage.twonum;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * Function:两个线程交替执行打印 1~100 等待通知机制版
 * 取材于https://github.com/crossoverJie/ 为方便阅读略有修改
 */
@Slf4j
public class TwoNumWaitNotify {
    private final    Object lock = new Object();
    private volatile int    num  = 1;

    public class EvenRunnable implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            synchronized (lock) {
                while (num <= 100) {
                    while ((num & 1) == 1) {
                        lock.wait();
                    }
                    log.info("偶数:{}", num++);
                    lock.notify();
                }
            }
        }
    }

    public class OddRunnable implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            synchronized (lock) {
                while (num < 100) {
                    while ((num & 1) == 0) {
                        lock.wait();
                    }
                    log.info("奇数:{}", num++);
                    lock.notify();
                }
            }
        }
    }

    public static void main(String[] args) {
        TwoNumWaitNotify twoNum = new TwoNumWaitNotify();
        new Thread(twoNum.new OddRunnable(), "t1").start();
        new Thread(twoNum.new EvenRunnable(), "t2").start();
    }
}
