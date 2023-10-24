package org.luvx.coding.jdk.concurrent.notify.pc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Case2 {
    private static volatile Integer count = 0;
    private static final    Integer FULL  = 10;

    private final Lock      lock     = new ReentrantLock();
    private final Condition notFull  = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    class Producer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                lock.lock();
                try {
                    while (count > FULL) {
                        notFull.await();
                    }
                    log.info("生产者({})生产, {} -> {}", Thread.currentThread().getName(), count, ++count);
                    notEmpty.signal();
                } finally {
                    lock.unlock();
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
                lock.lock();
                try {
                    while (count <= 0) {
                        notEmpty.await();
                    }
                    log.info("消费者({})消费, {} -> {}", Thread.currentThread().getName(), count, --count);
                    notFull.signal();
                } finally {
                    lock.unlock();
                }
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }
}
