package org.luvx.concurrent.notify.pc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author: Ren, Xie
 */
@Slf4j
public class Case1 {
    private static       Integer count = 0;
    private static final Integer FULL  = 10;

    private static final Object lock = new Object();

    class Producer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    while (count > FULL) {
                        lock.wait();
                    }
                    log.info("生产者({})生产, {} -> {}", Thread.currentThread().getName(), count, ++count);
                    if (count > FULL) {
                        lock.notifyAll();
                    }
                }
                TimeUnit.SECONDS.sleep(2);
            }
        }
    }

    class Consumer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    while (count <= 0) {
                        lock.wait();
                    }
                    log.info("消费者({})消费, {} -> {}", Thread.currentThread().getName(), count, --count);
                    if (count <= 0) {
                        lock.notifyAll();
                    }
                }
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }
}
