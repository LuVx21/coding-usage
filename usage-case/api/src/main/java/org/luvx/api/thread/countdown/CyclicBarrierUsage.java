package org.luvx.api.thread.countdown;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * CyclicBarrier:类似于CountDownLatch
 * 阻塞子进程,区别于CountDownLatch的阻塞主进程
 */
@Slf4j
public class CyclicBarrierUsage {
    private static final int THREAD_COUNT_NUM = 7;

    public static void main(String[] args) {
        //设置第一个屏障点，等待召集齐7位法师
        CyclicBarrier callMasterBarrier = new CyclicBarrier(THREAD_COUNT_NUM, () -> {
            log.info("7个法师召集完毕，同时出发，去往不同地方寻找龙珠！");
            // 放开可以增加多个屏障
            // summonDragon();
        });
        //召集齐7位法师
        for (int i = 1; i <= THREAD_COUNT_NUM; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(finalI * 100);
                    log.info("召集到第{}个法师", finalI);
                    callMasterBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        callMasterBarrier.reset();
    }

    private static void summonDragon() {
        // 设置第二个屏障点，等待7位法师收集完7颗龙珠，召唤神龙
        CyclicBarrier summonDragonBarrier = new CyclicBarrier(THREAD_COUNT_NUM, () -> {
            log.info("集齐七颗龙珠！召唤神龙！");
        });
        // 收集7颗龙珠
        for (int i = 1; i <= THREAD_COUNT_NUM; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(finalI * 100);
                    log.info("收集到第{}颗龙珠", finalI);
                    summonDragonBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
