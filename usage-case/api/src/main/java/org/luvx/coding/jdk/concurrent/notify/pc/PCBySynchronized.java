package org.luvx.coding.jdk.concurrent.notify.pc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.luvx.coding.jdk.concurrent.utils.ThreadUtils;

import java.util.concurrent.TimeUnit;

/**
 * 生产者消费者: synchronized 实现
 */
@Slf4j
public class PCBySynchronized {
    private static final Integer FULL = 10;

    private Integer count = 0;

    private final Object lock = new Object();

    class Producer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    while (count >= FULL) {
                        lock.wait();
                    }
                    log.info("生产者({})生产, {} -> {}", Thread.currentThread().getName(), count, ++count);
                    lock.notifyAll();
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
                    lock.notifyAll();
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
