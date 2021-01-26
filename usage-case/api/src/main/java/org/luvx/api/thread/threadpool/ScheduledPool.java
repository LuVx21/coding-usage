package org.luvx.api.thread.threadpool;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ScheduledPool {

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
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
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
