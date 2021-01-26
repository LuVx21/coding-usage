package org.luvx.algorithm.queue;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阻塞式队列
 * 1. 链表作为存储容器
 * 2. ReentrantLock 作为同步工具
 * 3. Condition 进行线程通信
 */
public class BlockQueue {

    private final LinkedList<Object> list    = new LinkedList<>();
    private final AtomicInteger      count   = new AtomicInteger(0);
    private final int                MAXSIZE = 5;
    private final int                MINSIZE = 0;

    private final Lock      lock     = new ReentrantLock();
    private       Condition notEmpty = lock.newCondition();
    private       Condition notFull  = lock.newCondition();

    public void put(Object obj) {
        lock.lock();
        while (count.get() == MAXSIZE) {
            try {
                notFull.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        list.add(obj);
        count.getAndIncrement();
        System.out.println(" 元素 " + obj + " 被添加 ");
        notEmpty.signal();
        lock.unlock();
    }

    public Object get() {
        lock.lock();
        Object temp;
        while (count.get() == MINSIZE) {
            try {
                notEmpty.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        count.getAndDecrement();
        temp = list.removeFirst();
        System.out.println(" 元素 " + temp + " 被移出 ");
        notFull.signal();
        lock.unlock();
        return temp;
    }

    public int size() {
        return count.get();
    }
}