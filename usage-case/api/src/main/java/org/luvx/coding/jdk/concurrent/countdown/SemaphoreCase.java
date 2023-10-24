package org.luvx.coding.jdk.concurrent.countdown;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

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

    private static final ExecutorService executor = Executors.newFixedThreadPool(100);

    @SneakyThrows
    public void run(int i) {
        try {
            semaphore.acquire();
            log.info("第{}个人业务start...", i);
            TimeUnit.SECONDS.sleep(2);
            log.info("第{}个人业务end...", i);
        } finally {
            semaphore.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SemaphoreCase exec = new SemaphoreCase();
        IntStream.rangeClosed(1, num).forEach(i ->
                executor.execute(() -> exec.run(i))
        );
        executor.shutdown();
    }
}
