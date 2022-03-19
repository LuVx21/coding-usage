package org.luvx.thread.usage.twonum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TwoNumNonBlock {

    static class Impl implements Runnable {
        /**
         * 当flag为1时只有奇数线程可以执行，并将其置为0
         * 当flag为0时只有偶数线程可以执行，并将其置为1
         */
        private volatile static int flag = 1;
        private                 int num;

        private Impl(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            while (num <= 100) {
                if ((num & 1) == flag) {
                    log.info("{}线程:{}", Thread.currentThread().getName(), num);
                    num += 2;
                    flag ^= 1;
                }
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new Impl(1), "奇数线程").start();
        new Thread(new Impl(2), "偶数线程").start();
    }
}