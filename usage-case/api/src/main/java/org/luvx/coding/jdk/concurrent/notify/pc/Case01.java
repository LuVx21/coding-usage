package org.luvx.coding.jdk.concurrent.notify.pc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author: Ren, Xie
 */
@Slf4j
public class Case01 {
    private static volatile Integer count = 0;
    private static final    Integer FULL  = 10;

    private static final Object producerLock = new Object();
    private static final Object consumerLock = new Object();

    class Producer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                synchronized (producerLock) {
                    while (count > FULL) {
                        producerLock.wait();
                    }
                    log.info("生产者({})生产, {} -> {}", Thread.currentThread().getName(), count, ++count);
                    // if (count < FULL) {
                    //     producerLock.notifyAll();
                    // }
                }
                if (count > 0) {
                    synchronized (consumerLock) {
                        consumerLock.notifyAll();
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
                synchronized (consumerLock) {
                    while (count <= 0) {
                        consumerLock.wait();
                    }
                    log.info("消费者({})消费, {} -> {}", Thread.currentThread().getName(), count, --count);
                    // if (count > 0) {
                    //     consumerLock.notifyAll();
                    // }
                }
                if (count < FULL) {
                    synchronized (producerLock) {
                        producerLock.notifyAll();
                    }
                }
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }
}
