package org.luvx.coding.jdk.concurrent._volatile;

import java.util.ArrayList;
import java.util.List;

/**
 * volatile关键字性能比较
 * volatile是一个轻量级synchronized, 性能稍好
 */
public class VolatilePerformance {

    /**
     * 不使用volatile
     */
    private class Count11 {
        private int value;

        public synchronized int getValue() {
            return value;
        }

        public synchronized int increment() {
            return value++;
        }
    }

    /**
     * 使用volatile
     */
    private class Count12 {
        private volatile int value = 0;

        int getValue() {
            return value;
        }

        synchronized int increment() {
            return value++;
        }
    }

    public static void main(String[] args) throws Exception {
        VolatilePerformance.Count11 count11 = new VolatilePerformance().new Count11();
        List<Thread> threadArrayList = new ArrayList<>();
        final int[] a = {0};
        Long allTime = 0l;
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 4; i++) {
            Thread t = new Thread(() -> {
                int b = 0;
                for (int j = 0; j < 10000; j++) {
                    count11.increment();
                    a[0] = count11.getValue();
                }
                for (int j = 0; j < 10000; j++) {
                    b++;
                    a[0] = count11.getValue();
                }
            });
            t.start();
            threadArrayList.add(t);
        }

        for (Thread t : threadArrayList) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        allTime = ((endTime - startTime));
        System.out.println("result: " + a[0] + ", average time: " + (allTime) + "ms");
    }

}
