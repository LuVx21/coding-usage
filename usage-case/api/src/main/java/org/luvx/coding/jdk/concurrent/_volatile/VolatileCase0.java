package org.luvx.concurrent._volatile;

import java.util.concurrent.TimeUnit;

/**
 * 演示volatile的效果
 * 使用其 保证可见性的特点
 * 不使用 volatile 可能会出现死循环无法退出的情形
 */
public class VolatileCase0 {
    private static boolean stop = false;
    // private static volatile boolean stop;

    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            int i = 0;
            while (!stop) {
                i++;
            }
            System.out.println(i);
        }).start();

        TimeUnit.MILLISECONDS.sleep(5);
        System.out.println("+++++++");
        stop = true;
        System.out.println(stop);
        System.out.println("-------");
    }
}
