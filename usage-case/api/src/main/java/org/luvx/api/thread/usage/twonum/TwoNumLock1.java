package org.luvx.api.thread.usage.twonum;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j

public class TwoNumLock1 {
    private Lock      lock          = new ReentrantLock();
    private Condition evenCondition = lock.newCondition();
    private Condition oddCondition  = lock.newCondition();
    private int       num           = 1;

    private class EvenRunnable implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            lock.lock();
            try {
                while (num <= 100) {
                    while ((num & 1) != 0) {
                        oddCondition.await();
                    }
                    log.info("偶数:{}", num++);
                    evenCondition.signalAll();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private class OddRunnable implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            lock.lock();
            try {
                while (num < 100) {
                    while ((num & 1) == 0) {
                        evenCondition.await();
                    }
                    log.info("奇数:{}", num++);
                    oddCondition.signalAll();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        TwoNumLock1 t = new TwoNumLock1();
        new Thread(t.new EvenRunnable()).start();
        new Thread(t.new OddRunnable()).start();
    }
}
