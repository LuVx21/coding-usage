package org.luvx.coding.jdk.concurrent.countdown;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CyclicBarrierCase1 {
    private static final int THREAD_COUNT_NUM = 10;

    private final CyclicBarrier callMasterBarrier = new CyclicBarrier(THREAD_COUNT_NUM, () -> {
        log.info("{}个线程全部完成,继续...", THREAD_COUNT_NUM);
        // 继续执行await()后的逻辑
    });

    @AllArgsConstructor
    class A implements Runnable {
        private int i;

        @SneakyThrows
        @Override
        public void run() {
            log.info("第{}个线程开始, 线程:{}", i, Thread.currentThread().getName());
            TimeUnit.MILLISECONDS.sleep(i * 100L);
            callMasterBarrier.await();
            log.info("第{}个线程结束, 线程:{}", i, Thread.currentThread().getName());
        }
    }

    private void a() {
        for (int i = 1; i <= THREAD_COUNT_NUM; i++) {
            new Thread(new A(i)).start();
        }
    }

    private void b() {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < THREAD_COUNT_NUM; i++) {
            executor.execute(new A(i));
        }
        executor.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        CyclicBarrierCase1 exec = new CyclicBarrierCase1();
        exec.a();
        // exec.b();
    }
}
