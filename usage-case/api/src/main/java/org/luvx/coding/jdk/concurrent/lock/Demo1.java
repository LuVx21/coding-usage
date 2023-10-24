package org.luvx.coding.jdk.concurrent.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Demo1 {
    /**
     * 1. 主线程获得了锁，且最后执行死循环，所以永远不释放锁。
     * 2. 在主线程sleep期间，子线程已经阻塞在 java.util.concurrent.locks.AbstractQueuedSynchronizer#parkAndCheckInterrupt() 里了。
     * 3. 主线程sleep结束，中断子线程。但主线程不释放锁，那子线程被阻塞了为什么还执行了后续代码抛出InterruptedException
     * <p>
     * A: interrupt()也会使线程从阻塞状态中恢复
     */
    public static void main(String[] args) throws InterruptedException {
        final Lock lock = new ReentrantLock();
        lock.lock();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.lockInterruptibly();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.info("线程{}中断", Thread.currentThread().getName());
                }
                log.info("线程{}结束了", Thread.currentThread().getName());
            }
        });

        t1.start();
        TimeUnit.SECONDS.sleep(5);
        t1.interrupt();
        while (true) {
        }
    }
}
