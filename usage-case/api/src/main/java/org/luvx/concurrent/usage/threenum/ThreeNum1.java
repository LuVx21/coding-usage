package org.luvx.concurrent.usage.threenum;

import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 三个线程循环打印ABC: synchronized 实现
 */
@Slf4j
public class ThreeNum1 {

    @SneakyThrows
    public static void main(String[] args) {
        final Object a = new Object(), b = new Object(), c = new Object();
        ThreeNum1 exec = new ThreeNum1();
        new Thread(() -> exec.run(c, a, 'A'), "t-A").start();
        TimeUnit.MILLISECONDS.sleep(10);
        new Thread(() -> exec.run(a, b, 'B'), "t-B").start();
        TimeUnit.MILLISECONDS.sleep(10);
        new Thread(() -> exec.run(b, c, 'C'), "t-C").start();
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    @SneakyThrows
    private void run(Object pre, Object cur, char ch) {
        int cnt = 10;
        while (cnt > 0) {
            synchronized (pre) {
                synchronized (cur) {
                    log.info("线程:{}, 输出:{}", Thread.currentThread().getName(), ch);
                    cnt--;
                    cur.notify();
                }
                if (cnt == 0) {
                    pre.notify();
                } else {
                    pre.wait();
                }
            }
        }
    }
}
