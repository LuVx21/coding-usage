package org.luvx.coding.jdk.concurrent.lock;

import java.util.concurrent.locks.StampedLock;

public class StampedLockCase2 {
    private int x, y;

    final StampedLock lock = new StampedLock();

    // 计算到原点的距离
    private double distanceFromOrigin() {
        // 乐观读
        long stamp = lock.tryOptimisticRead();
        // 读入局部变量，
        // 读的过程数据可能被修改
        int curX = x, curY = y;
        // 判断执行读操作期间，是否存在写操作，如果存在，则 sl.validate 返回 false
        if (!lock.validate(stamp)) {
            // 升级为悲观读锁
            stamp = lock.readLock();
            try {
                curX = x;
                curY = y;
            } finally {
                // 释放悲观读锁
                lock.unlockRead(stamp);
            }
        }

        return Math.sqrt(curX * curX + curY * curY);
    }

}
