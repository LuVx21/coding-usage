package org.luvx.coding.jdk.concurrent.usage.twonum;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Ren, Xie
 * 循环一直在跑
 */
@Slf4j
public class TwoNumNonBlockAtomic {
    AtomicInteger i = new AtomicInteger(0);

    /**
     * 偶数打印线程
     */
    private class EvenRunnable implements Runnable {
        @Override
        public void run() {
            int num = 0;
            while ((num = i.get()) <= 100) {
                if ((num & 1) == 0) {
                    log.info("偶数线程:{}", num);
                    i.getAndIncrement();
                }
            }
        }
    }

    /**
     * 奇数打印线程
     */
    private class OddRunnable implements Runnable {
        @Override
        public void run() {
            int num = 0;
            while ((num = i.get()) <= 100) {
                if ((num & 1) != 0) {
                    log.info("奇数线程:{}", num);
                    i.getAndIncrement();
                }
            }
        }
    }

    public static void main(String[] args) {
        TwoNumNonBlockAtomic e = new TwoNumNonBlockAtomic();
        new Thread(e.new EvenRunnable()).start();
        new Thread(e.new OddRunnable()).start();
    }
}
