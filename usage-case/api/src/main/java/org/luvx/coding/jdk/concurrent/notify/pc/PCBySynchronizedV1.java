package org.luvx.coding.jdk.concurrent.notify.pc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.luvx.coding.jdk.concurrent.utils.ThreadUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者消费者: synchronized 实现
 */
@Slf4j
public class PCBySynchronizedV1 {
    private static final Integer FULL = 10;

    private final AtomicInteger count = new AtomicInteger(0);

    private final Object producerLock = new Object();
    private final Object consumerLock = new Object();

    class Producer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                synchronized (producerLock) {
                    while (count.get() >= FULL) {
                        producerLock.wait();
                    }
                    log.info("生产者({})生产, {} -> {}", Thread.currentThread().getName(), count, count.incrementAndGet());
                    // if (count < FULL) {
                    //     producerLock.notifyAll();
                    // }
                }
                if (count.get() > 0) {
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
                    while (count.get() <= 0) {
                        consumerLock.wait();
                    }
                    log.info("消费者({})消费, {} -> {}", Thread.currentThread().getName(), count, count.decrementAndGet());
                    // if (count > 0) {
                    //     consumerLock.notifyAll();
                    // }
                }
                if (count.get() < FULL) {
                    synchronized (producerLock) {
                        producerLock.notifyAll();
                    }
                }
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    public void main() {
        for (int i = 0; i < 1; i++) {
            ThreadUtils.SERVICE.execute(new Consumer());
        }
        for (int i = 0; i < 4; i++) {
            ThreadUtils.SERVICE.execute(new Producer());
        }
        ThreadUtils.SERVICE.shutdown();
    }
}
