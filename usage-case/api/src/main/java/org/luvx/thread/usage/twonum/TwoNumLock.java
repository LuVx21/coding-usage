package org.luvx.thread.usage.twonum;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Function: 两个线程交替执行打印 1~100 lock 版
 * 取材于https://github.com/crossoverJie/ 为方便阅读略有修改
 */
@Slf4j
public class TwoNumLock {
    private final    Lock      lock      = new ReentrantLock();
    private final    Condition condition = lock.newCondition();
    private volatile int       num       = 1;

    public class EvenRunnable implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            lock.lock();
            try {
                while (num <= 100) {
                    while ((num & 1) != 0) {
                        condition.await();
                    }
                    log.info("偶数:{}", num++);
                    condition.signal();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public class OddRunnable implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            lock.lock();
            try {
                while (num < 100) {
                    while ((num & 1) != 1) {
                        condition.await();
                    }
                    log.info("奇数:{}", num++);
                    condition.signal();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        TwoNumLock twoNum = new TwoNumLock();
        new Thread(twoNum.new OddRunnable(), "t1").start();
        new Thread(twoNum.new EvenRunnable(), "t2").start();
    }
}
