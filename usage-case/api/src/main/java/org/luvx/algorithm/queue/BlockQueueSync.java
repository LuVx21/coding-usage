package org.luvx.algorithm.queue;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 阻塞式队列
 * 1. 链表作为存储容器
 * 2. synchronized 作为同步工具
 * 3. wait/notify 进行线程通信
 */
public class BlockQueueSync {

    private final LinkedList<Object> list    = new LinkedList<>();
    private final AtomicInteger      count   = new AtomicInteger(0);
    private final int                MAXSIZE = 5;
    private final int                MINSIZE = 0;

    private final Object lock = new Object();

    public void put(Object obj) {
        synchronized (lock) {
            while (count.get() == MAXSIZE) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.add(obj);
            count.getAndIncrement();
            System.out.println(" 元素 " + obj + " 被添加 ");
            lock.notify();
        }
    }

    public Object get() {
        Object temp;
        synchronized (lock) {
            while (count.get() == MINSIZE) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count.getAndDecrement();
            temp = list.removeFirst();
            System.out.println(" 元素 " + temp + " 被移出 ");
            lock.notify();
        }
        return temp;
    }

    public int size() {
        return count.get();
    }
}