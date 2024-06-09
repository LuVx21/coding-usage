package org.luvx.coding.jdk.concurrent.notify.pc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.luvx.coding.common.concurrent.Threads;
import org.luvx.coding.jdk.concurrent.utils.ThreadUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 生产者消费者: ReentrantReadWriteLock 实现
 */
@Slf4j
public class PCByReentrantReadWriteLock {
    private static final Integer FULL = 10;

    private final AtomicInteger count = new AtomicInteger(0);

    final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    class Producer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                lock.readLock().lock();
                try {
                    if (count.get() < FULL) {
                        log.info("生产者({})生产, {} -> {}", Thread.currentThread().getName(), count.get(), count.incrementAndGet());
                    }
                } finally {
                    lock.readLock().unlock();
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
                lock.writeLock().lock();
                try {
                    if (count.get() > 0) {
                        log.info("消费者({})消费, {} -> {}", Thread.currentThread().getName(), count.get(), count.decrementAndGet());
                    }
                } finally {
                    lock.writeLock().unlock();
                }
                TimeUnit.SECONDS.sleep(4);
            }
        }
    }

    class PC implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            lock.readLock().lock();
            if (count.get() <= 0) {
                // 读写互斥，必需读锁后才能加写锁
                lock.readLock().unlock();
                lock.writeLock().lock();
                try {
                    // 得到写锁后再判断，防止被其它线程抢先更改
                    if (count.get() <= 0) {
                        log.info("生产者({})生产, {} -> {}", Thread.currentThread().getName(), count.get(), count.incrementAndGet());
                        Thread.sleep(2);
                    }
                } finally {
                    // 释放写锁前,先加读锁,防止使用之前被其他写线程改变了数据状态
                    lock.readLock().lock();
                    lock.writeLock().unlock();
                }
            }
            try {
                log.info("消费者({})消费, {} -> {}", Thread.currentThread().getName(), count.get(), count.decrementAndGet());
                Threads.sleep(TimeUnit.MILLISECONDS, 1000);
            } finally {
                lock.readLock().unlock();
            }
        }
    }


    public void main() {
        // for (int i = 0; i < 2; i++) {
        //     MoreRuns.runThread(new Consumer(), STR."消费者\{i + 1}");
        // }
        // for (int i = 0; i < 2; i++) {
        //     MoreRuns.runThread(new Producer(), STR."生产者\{i + 1}");
        // }

        for (int i = 0; i < 5; i++) {
            ThreadUtils.SERVICE.execute(new PC());
        }
        ThreadUtils.SERVICE.shutdown();
    }
}
