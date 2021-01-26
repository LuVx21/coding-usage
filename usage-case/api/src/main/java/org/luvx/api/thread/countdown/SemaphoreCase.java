package org.luvx.api.thread.countdown;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 例子: 银行窗口排队
 *
 * @author: Ren, Xie
 */
@Slf4j
public class SemaphoreCase {
    /**
     * 客户数
     */
    private static final int       num       = 10;
    /**
     * 银行窗口数
     */
    private static final int       n1        = 3;
    private static final Semaphore semaphore = new Semaphore(n1);

    private static final ExecutorService executor = Executors.newFixedThreadPool(n1 + 1);

    @AllArgsConstructor
    static class A implements Runnable {
        private final int i;

        @SneakyThrows
        @Override
        public void run() {
            try {
                semaphore.acquire();
                log.info("第{}个人开始办理业务", i);
                TimeUnit.SECONDS.sleep(2);
                log.info("第{}个人业务办理结束", i);
            } finally {
                semaphore.release();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= num; i++) {
            executor.execute(new A(i));
            TimeUnit.MILLISECONDS.sleep(500);
        }
        executor.shutdown();
    }
}
