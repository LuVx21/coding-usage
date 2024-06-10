package org.luvx.coding.jdk.concurrent.countdown;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.luvx.coding.common.concurrent.Threads;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CyclicBarrier:类似于CountDownLatch
 * 阻塞子进程,区别于CountDownLatch的阻塞主进程
 */
@Slf4j
public class CyclicBarrierCase2 {
    private static final int THREAD_COUNT_NUM = 7;

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    // 设置第一个屏障点，等待召集齐7位法师
    CyclicBarrier callMasterBarrier = new CyclicBarrier(THREAD_COUNT_NUM, () -> {
        log.info("7个法师召集完毕，同时出发，去往不同地方寻找龙珠！");
        // 放开可以增加多个屏障
        for (int i = 1; i <= THREAD_COUNT_NUM; i++) {
            executor.execute(new B(i));
        }
    });

    // 设置第二个屏障点，等待7位法师收集完7颗龙珠，召唤神龙
    CyclicBarrier summonDragonBarrier = new CyclicBarrier(THREAD_COUNT_NUM, () -> {
        log.info("集齐七颗龙珠！召唤神龙！");
    });

    @AllArgsConstructor
    class A implements Runnable {
        private int i;

        @SneakyThrows
        @Override
        public void run() {
            TimeUnit.MILLISECONDS.sleep(i * 100L);
            log.info("召集到第{}个法师", i);
            callMasterBarrier.await();
        }
    }

    @AllArgsConstructor
    class B implements Runnable {
        private int i;

        @SneakyThrows
        @Override
        public void run() {
            TimeUnit.MILLISECONDS.sleep(i * 100L);
            log.info("收集到第{}颗龙珠", i);
            summonDragonBarrier.await();
        }
    }

    public void main(String[] args) {
        for (int i = 1; i <= THREAD_COUNT_NUM; i++) {
            executor.execute(new A(i));
        }

        Threads.sleep(3_000);
        executor.shutdown();
    }
}
