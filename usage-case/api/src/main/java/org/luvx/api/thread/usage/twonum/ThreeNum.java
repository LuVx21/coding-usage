package org.luvx.api.thread.usage.twonum;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 三个线程循环打印ABC
 */
@Slf4j
public class ThreeNum {

    public static void main(String[] args) {
        ThreeNum threeNum = new ThreeNum();
        new Thread(threeNum.new A(), "t-A").start();
        new Thread(threeNum.new B(), "t-B").start();
        new Thread(threeNum.new C(), "t-C").start();
    }

    private AtomicInteger num = new AtomicInteger(0);

    class A implements Runnable {
        @Override
        public void run() {
            while (num.get() < 10) {
                if (num.get() % 3 == 0 && num.get() < 10) {
                    log.info("线程:{}, 输出:{}", Thread.currentThread().getName(), (char) (num.get() % 3 + 65));
                    num.incrementAndGet();
                }
            }
        }
    }

    class B implements Runnable {
        @Override
        public void run() {
            while (num.get() < 10) {// 此处10有可能进入,B判断恰好是A将要自增的时候,仍为9,B进入循环后A将num变为10
                if (num.get() % 3 == 1 && num.get() < 10) {
                    log.info("线程:{}, 输出:{}", Thread.currentThread().getName(), (char) (num.get() % 3 + 65));
                    num.incrementAndGet();
                }
            }
        }
    }

    class C implements Runnable {
        @Override
        public void run() {
            while (num.get() < 10) {
                if (num.get() % 3 == 2 && num.get() < 10) {
                    log.info("线程:{}, 输出:{}", Thread.currentThread().getName(), (char) (num.get() % 3 + 65));
                    num.incrementAndGet();
                }
            }
        }
    }
}
