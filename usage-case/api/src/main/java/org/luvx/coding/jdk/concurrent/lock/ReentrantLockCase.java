package org.luvx.coding.jdk.concurrent.lock;

import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReentrantLock:完全互斥排他
 * <p/>
 * ReentrantReadWriteLock:可多线程同时读,但只允许一个线程写(即只要有写就互斥)
 * 读操作:共享锁
 * 写操作:排它锁
 */
public class ReentrantLockCase {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @SneakyThrows
    private void read() {
        try {
            lock.readLock().lock();
            System.out.println(Thread.currentThread().getName() + "获得读锁 时间:" + LocalDateTime.now());
            // 模拟读操作时间为5秒
            TimeUnit.SECONDS.sleep(5);
        } finally {
            lock.readLock().unlock();
        }
    }

    @SneakyThrows
    private void write() {
        try {
            lock.writeLock().lock();
            System.out.println(Thread.currentThread().getName() + "获得写锁 时间:" + LocalDateTime.now());
            // 模拟写操作时间为5秒
            TimeUnit.SECONDS.sleep(5);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockCase rlCase = new ReentrantLockCase();
        // 读读不互斥
        // new Thread(rlCase::read, "thread1").start();
        // new Thread(rlCase::read, "thread2").start();

        // 写写互斥
        // new Thread(rlCase::write, "thread3").start();
        // new Thread(rlCase::write, "thread4").start();

        // 读写互斥
        new Thread(rlCase::write, "thread5").start();
        // Thread.sleep(1000);
        new Thread(rlCase::read, "thread6").start();
    }
}
