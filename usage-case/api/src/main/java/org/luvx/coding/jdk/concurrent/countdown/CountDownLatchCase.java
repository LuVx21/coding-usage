package org.luvx.concurrent.countdown;

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
    private static final int            THREAD_COUNT_NUM = 10;
    private static final CountDownLatch countDownLatch   = new CountDownLatch(10);

    @AllArgsConstructor
    static class A implements Runnable {
        private int i;

        @SneakyThrows
        @Override
        public void run() {
            TimeUnit.MILLISECONDS.sleep(i * 100);
            // 每个线程结束后执行
            log.info("第{}个人到了, 线程:{}", i, Thread.currentThread().getName());
            countDownLatch.countDown();
            log.info("第{}个人进门, 线程:{}", i, Thread.currentThread().getName());
        }
    }

    private static void a() throws InterruptedException {
        for (int i = 0; i < THREAD_COUNT_NUM; i++) {
            new Thread(new A(i)).start();
        }
        // 所有线程结束后,倒计时完成开始执行
        countDownLatch.await();

        TimeUnit.SECONDS.sleep(1);
        log.info("10人全到了");
    }

    private static void b() throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < THREAD_COUNT_NUM; i++) {
            executor.execute(new A(i));
        }
        // 所有线程结束后,倒计时完成开始执行
        countDownLatch.await();

        TimeUnit.SECONDS.sleep(1);
        log.info("10人全到了");
        executor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        a();
        // b();
    }
}
