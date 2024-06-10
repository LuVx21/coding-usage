package org.luvx.coding.jdk.concurrent.countdown;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 演示 reset 用法
 * 例子: 约人一起打球
 */
@Slf4j
public class CyclicBarrierCase3 {
    private static final int NUM = 4;

    private final CyclicBarrier cb = new CyclicBarrier(NUM, () -> log.info("集合完毕, 出发去球场"));

    @AllArgsConstructor
    class A implements Runnable {
        private final int i;

        @SneakyThrows
        @Override
        public void run() {
            TimeUnit.MILLISECONDS.sleep(i * 100L);
            log.info("第{}个人到了楼下", i);
            cb.await();
            log.info("第{}个人从楼下出发", i);
        }
    }

    public void main(String[] args) throws InterruptedException {
        final ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 1; i <= NUM; i++) {
            executor.execute(new A(i));
        }

        TimeUnit.SECONDS.sleep(2);
        log.info("1~4到达球场, 开始打球");
        TimeUnit.SECONDS.sleep(2);
        log.info("再约另外5~8等4个人打球");

        cb.reset();

        for (int i = NUM + 1; i <= NUM << 1; i++) {
            executor.execute(new A(i));
        }

        TimeUnit.SECONDS.sleep(2);
        log.info("5~8到达球场, 1~8一起打球");

        executor.shutdown();
    }
}