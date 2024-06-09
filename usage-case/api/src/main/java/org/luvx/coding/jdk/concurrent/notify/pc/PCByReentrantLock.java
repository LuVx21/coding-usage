package org.luvx.coding.jdk.concurrent.notify.pc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.luvx.coding.jdk.concurrent.utils.ThreadUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者消费者: ReentrantLock 实现
 */
@Slf4j
public class PCByReentrantLock {
    private static final Integer FULL = 10;

    private final AtomicInteger count = new AtomicInteger(0);

    private final Lock      lock      = new ReentrantLock();
    private final Condition cProducer = lock.newCondition();
    private final Condition cConsumer = lock.newCondition();

    class Producer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                lock.lock();
                try {
                    while (count.get() >= FULL) {
                        cProducer.await();
                    }
                    log.info("生产者({})生产, {} -> {}", Thread.currentThread().getName(), count.get(), count.incrementAndGet());
                    cConsumer.signalAll();
                } finally {
                    lock.unlock();
                }
                TimeUnit.SECONDS.sleep(6);
            }
        }
    }

    class Consumer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                lock.lock();
                try {
                    while (count.get() <= 0) {
                        cConsumer.await();
                    }
                    log.info("消费者({})消费, {} -> {}", Thread.currentThread().getName(), count.get(), count.decrementAndGet());
                    cProducer.signalAll();
                } finally {
                    lock.unlock();
                }
                TimeUnit.SECONDS.sleep(3);
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
