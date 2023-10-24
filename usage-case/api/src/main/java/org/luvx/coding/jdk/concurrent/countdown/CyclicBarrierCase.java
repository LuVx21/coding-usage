package org.luvx.coding.jdk.concurrent.countdown;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CyclicBarrierCase {
    private static final int           THREAD_COUNT_NUM  = 10;
    private static       CyclicBarrier callMasterBarrier = new CyclicBarrier(THREAD_COUNT_NUM, () -> {
        log.info("10人全到了, 开门放行");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    });

    @AllArgsConstructor
    static class A implements Runnable {
        private final int i;

        @SneakyThrows
        @Override
        public void run() {
            TimeUnit.MILLISECONDS.sleep(i * 100);
            log.info("第{}个人到了, 线程:{}", i, Thread.currentThread().getName());
            callMasterBarrier.await();
            log.info("第{}个人进门, 线程:{}", i, Thread.currentThread().getName());
        }
    }

    private static void a() {
        for (int i = 1; i <= THREAD_COUNT_NUM; i++) {
            new Thread(new A(i)).start();
        }
    }

    private static void b() {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < THREAD_COUNT_NUM; i++) {
            executor.execute(new A(i));
        }
        executor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        a();
        // b();
    }
}
