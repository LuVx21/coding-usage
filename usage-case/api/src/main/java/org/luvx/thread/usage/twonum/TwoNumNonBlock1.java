package org.luvx.thread.usage.twonum;

import lombok.extern.slf4j.Slf4j;
/**
 * @author: Ren, Xie
 */
@Slf4j
public class TwoNumNonBlock1 {
    private static volatile int num = 1;

    static class EvenRunnable implements Runnable {
        @Override
        public void run() {
            while (num <= 100) {
                if ((num & 1) == 0) {
                    log.info("偶数:{}", num);
                    synchronized (this) {
                        num++;
                    }
                }
            }
        }
    }

    static class OddRunnable implements Runnable {
        @Override
        public void run() {
            while (num < 100) {
                if ((num & 1) == 1) {
                    log.info("奇数:{}", num);
                    synchronized (this) {
                        num = num + 1;
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(new EvenRunnable()).start();
        new Thread(new OddRunnable()).start();
    }
}

