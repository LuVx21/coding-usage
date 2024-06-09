package org.luvx.coding.jdk.concurrent.countdown;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞主进程
 * 区别于CyclicBarrier的阻塞子进程
 *
 * @author renxie
 */
@Slf4j
public class CountDownLatchCase {
    private static final int THREAD_COUNT_NUM = 10;

    private final CountDownLatch countDownLatch = new CountDownLatch(10);

    @AllArgsConstructor
    class A implements Runnable {
        private int i;

        @SneakyThrows
        @Override
        public void run() {
            TimeUnit.MILLISECONDS.sleep(i * 100L);
            // 每个线程结束后执行
            countDownLatch.countDown();
            log.info("第{}个线程结束, 线程:{}", i, Thread.currentThread().getName());
        }
    }

    private void a() throws InterruptedException {
        for (int i = 0; i < THREAD_COUNT_NUM; i++) {
            new Thread(new A(i)).start();
        }
        // 所有线程结束后,倒计时完成开始执行
        countDownLatch.await();

        TimeUnit.SECONDS.sleep(1);
        log.info("10个全结束");
    }

    private void b() throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < THREAD_COUNT_NUM; i++) {
            executor.execute(new A(i));
        }
        // 所有线程结束后,倒计时完成开始执行
        countDownLatch.await();

        TimeUnit.SECONDS.sleep(1);
        log.info("10个全结束");
        executor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatchCase exec = new CountDownLatchCase();
        exec.a();
        // exec.b();
    }
}
