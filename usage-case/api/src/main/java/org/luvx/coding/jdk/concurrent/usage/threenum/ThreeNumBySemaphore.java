package org.luvx.coding.jdk.concurrent.usage.threenum;

import java.util.concurrent.Semaphore;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 三个线程循环打印ABC: Semaphore 实现
 */
@Slf4j
public class ThreeNumBySemaphore {

    public static void main(String[] args) {
        Semaphore A = new Semaphore(1), B = new Semaphore(0), C = new Semaphore(0);
        ThreeNumBySemaphore exec = new ThreeNumBySemaphore();
        new Thread(() -> exec.run(A, B, 'A'), "t-A").start();
        new Thread(() -> exec.run(B, C, 'B'), "t-B").start();
        new Thread(() -> exec.run(C, A, 'C'), "t-C").start();
    }

    @SneakyThrows
    private void run(Semaphore cur, Semaphore next, char ch) {
        for (int i = 0; i < 10; i++) {
            cur.acquire();
            log.info("线程:{}, 输出:{}", Thread.currentThread().getName(), ch);
            next.release();
        }
    }
}
