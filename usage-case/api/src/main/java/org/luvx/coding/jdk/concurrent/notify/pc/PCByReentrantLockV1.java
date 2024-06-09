package org.luvx.coding.jdk.concurrent.notify.pc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.luvx.coding.jdk.concurrent.utils.ThreadUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class PCByReentrantLockV1 {
    private static final Integer FULL = 10;

    private final AtomicInteger count = new AtomicInteger(0);

    private final Lock      producerLock = new ReentrantLock();
    private final Lock      consumerLock = new ReentrantLock();
    private final Condition cProducer    = producerLock.newCondition();
    private final Condition cConsumer    = consumerLock.newCondition();

    class Producer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                producerLock.lock();
                try {
                    while (count.get() >= FULL) {
                        cProducer.await();
                    }
                    log.info("生产者({})生产, {} -> {}", Thread.currentThread().getName(), count, count.incrementAndGet());
                    // if (count < FULL) {
                    //     notFull.signalAll();
                    // }
                } finally {
                    producerLock.unlock();
                }
                if (count.get() > 0) {
                    consumerLock.lock();
                    try {
                        cConsumer.signalAll();
                    } finally {
                        consumerLock.unlock();
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
                consumerLock.lock();
                try {
                    while (count.get() <= 0) {
                        cConsumer.await();
                    }
                    log.info("消费者({})消费, {} -> {}", Thread.currentThread().getName(), count, count.decrementAndGet());
                    // if (count > 0) {
                    //     notEmpty.signalAll();
                    // }
                } finally {
                    consumerLock.unlock();
                }
                if (count.get() < FULL) {
                    producerLock.lock();
                    try {
                        cProducer.signalAll();
                    } finally {
                        producerLock.unlock();
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
