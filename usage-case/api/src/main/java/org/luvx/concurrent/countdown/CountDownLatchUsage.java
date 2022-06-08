package org.luvx.concurrent.countdown;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch 模拟 CyclicBarrier 效果
 *
 * @author renxie
 */
@Slf4j
public class CountDownLatchUsage {
    private static final int NUM = 4;

    private static       CountDownLatch  cdl      = new CountDownLatch(NUM);
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    @AllArgsConstructor
    static class A implements Runnable {
        private final int i;

        @SneakyThrows
        @Override
        public void run() {
            TimeUnit.MILLISECONDS.sleep(i * 100);
            log.info("第{}个人到了楼下", i);
            cdl.countDown();
            if (cdl.getCount() == 0) {
                log.info("集合完毕, 出发去球场");
            }
            cdl.await();
            log.info("第{}个人从楼下出发", i);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= NUM; i++) {
            executor.execute(new A(i));
        }

        TimeUnit.SECONDS.sleep(2);
        log.info("1~4到达球场, 开始打球");
        TimeUnit.SECONDS.sleep(2);
        log.info("再约另外5~8等4个人打球");

        cdl = new CountDownLatch(NUM);

        for (int i = NUM + 1; i <= NUM << 1; i++) {
            executor.execute(new A(i));
        }

        TimeUnit.SECONDS.sleep(2);
        log.info("5~8到达球场, 1~8一起打球");

        executor.shutdown();
    }
}
