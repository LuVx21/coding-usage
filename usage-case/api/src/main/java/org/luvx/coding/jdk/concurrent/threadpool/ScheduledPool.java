package org.luvx.coding.jdk.concurrent.threadpool;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.*;

@Slf4j
public class ScheduledPool {
    static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);

    private static class TimerTask implements Runnable {
        @Override
        @SneakyThrows
        public void run() {
            log.info("{}任务开始, 当前时间：{}", hashCode(), LocalDateTime.now());
            TimeUnit.MILLISECONDS.sleep(2_000);
            log.info("{}任务结束, 当前时间：{}", hashCode(), LocalDateTime.now());
        }
    }

    public static void main(String[] args) {
        m0();
    }

    /**
     * 移除任务
     */
    @SneakyThrows
    private static void m0() {
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(5);

        RunnableScheduledFuture<?> f1 = (RunnableScheduledFuture<?>) executor.scheduleAtFixedRate(() -> {
            System.out.println("111 " + LocalDateTime.now());
        }, 0, 1, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(() -> {
            System.out.println("222 " + LocalDateTime.now());
        }, 0, 1, TimeUnit.SECONDS);


        // 10s后移除任务,不在调度执行
        TimeUnit.SECONDS.sleep(10);
        // executor.remove(f1);
        f1.cancel(true);

        TimeUnit.SECONDS.sleep(60);
    }

    private static void m1() {
        log.info("起始时间：{}", LocalDateTime.now());
        // 指定时间后执行
        executor.schedule(new TimerTask(), 2, TimeUnit.SECONDS);
        // 指定时间后执行
        executor.schedule(
                () -> {
                    new TimerTask().run();
                    return null;
                }, 5, TimeUnit.SECONDS
        );
        // initialDelay后开始, 每个period执行一次
        executor.scheduleAtFixedRate(new TimerTask(), 1, 3, TimeUnit.SECONDS);
        // initialDelay后开始, 执行结束后delay再次执行
        executor.scheduleWithFixedDelay(new TimerTask(), 1, 3, TimeUnit.SECONDS);
    }
}
