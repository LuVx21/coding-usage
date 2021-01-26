package org.luvx.api.thread.notify.pc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Case02 {
    private static volatile Integer count = 0;
    private static final    Integer FULL  = 10;

    private final Lock      producerLock = new ReentrantLock();
    private final Lock      consumerLock = new ReentrantLock();
    private final Condition notFull      = producerLock.newCondition();
    private final Condition notEmpty     = consumerLock.newCondition();

    class Producer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                producerLock.lock();
                try {
                    while (count > FULL) {
                        notFull.await();
                    }
                    log.info("生产者({})生产, {} -> {}", Thread.currentThread().getName(), count, ++count);
                    // if (count < FULL) {
                    //     notFull.signalAll();
                    // }
                } finally {
                    producerLock.unlock();
                }
                if (count > 0) {
                    consumerLock.lock();
                    try {
                        notEmpty.signalAll();
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
                    while (count <= 0) {
                        notEmpty.await();
                    }
                    log.info("消费者({})消费, {} -> {}", Thread.currentThread().getName(), count, --count);
                    // if (count > 0) {
                    //     notEmpty.signalAll();
                    // }
                } finally {
                    consumerLock.unlock();
                }
                if (count < FULL) {
                    producerLock.lock();
                    try {
                        notFull.signalAll();
                    } finally {
                        producerLock.unlock();
                    }
                }
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }
}
