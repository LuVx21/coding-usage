package org.luvx.api.thread._volatile;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * volatile 未能保证原子性
 * 能保证可见性和有序性
 */
public class VolatileCase {
    public volatile int inc = 0;

    public void increase() {
        // 自增操作不保证原子性
        inc++;
    }

    // 实现原子性方案1:同步
    /*
    public int inc = 0;

    public synchronized void increase() {
        inc++;
    }
    */

    // 实现原子性方案2:加锁
    /*
    public int inc = 0;
    Lock lock = new ReentrantLock();

    public void increase() {
        lock.lock();
        try {
            inc++;
        } finally {
            lock.unlock();
        }
    }
    */

    // 实现原子性方案3:原子操作
    /*
    public AtomicInteger inc = new AtomicInteger();

    public void increase() {
        inc.getAndIncrement();
    }
    */

    public static void main(String[] args) {
        final VolatileCase test = new VolatileCase();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    test.increase();
                }
            }).start();
        }

        // 保证前面的线程都执行完, 此处应大于2
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        method();
        System.out.println(test.inc);
    }

    /**
     * 上面为什么大于2才行
     */
    private static void method() {
        Thread.currentThread().getThreadGroup().list();

        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfo = bean.dumpAllThreads(false, false);
        for (ThreadInfo info : threadInfo) {
            System.out.println("[" + info.getThreadId() + "] " + info.getThreadName());
        }
    }
}
