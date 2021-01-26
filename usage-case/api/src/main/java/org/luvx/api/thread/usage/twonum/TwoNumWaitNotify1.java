package org.luvx.api.thread.usage.twonum;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TwoNumWaitNotify1 {
    private final    Object evenLock = new Object();
    private final    Object oddLock  = new Object();
    private volatile int    num      = 1;

    private class EvenRunnable implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (num <= 100) {
                synchronized (evenLock) {
                    while ((num & 1) == 1) {
                        evenLock.wait();
                    }
                    log.info("偶数:{}", num++);
                }
                synchronized (oddLock) {
                    oddLock.notifyAll();
                }
            }
        }
    }

    private class OddRunnable implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (num < 100) {
                synchronized (oddLock) {
                    while ((num & 1) == 0) {
                        oddLock.wait();
                    }
                    log.info("奇数:{}", num++);
                }
                synchronized (evenLock) {
                    evenLock.notifyAll();
                }
            }
        }
    }

    public static void main(String[] args) {
        TwoNumWaitNotify1 t = new TwoNumWaitNotify1();
        new Thread(t.new EvenRunnable(), "偶数").start();
        new Thread(t.new OddRunnable(), "奇数").start();
    }
}
