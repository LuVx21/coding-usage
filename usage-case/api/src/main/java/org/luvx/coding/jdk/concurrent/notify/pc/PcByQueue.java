package org.luvx.coding.jdk.concurrent.notify.pc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.luvx.coding.jdk.concurrent.utils.ThreadUtils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class PcByQueue {
    private volatile Integer count = 0;

    final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

    class Producer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                queue.put(1);
                log.info("生产者({})生产, {} -> {}", Thread.currentThread().getName(), count, ++count);
                TimeUnit.SECONDS.sleep(3);
            }
        }
    }

    class Consumer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                queue.take();
                log.info("消费者({})消费, {} -> {}", Thread.currentThread().getName(), count, --count);
                TimeUnit.SECONDS.sleep(3);
            }
        }
    }

    public void main() {
        for (int i = 0; i < 1; i++) {
            ThreadUtils.SERVICE.execute(new Consumer());
        }
        for (int i = 0; i < 4; i++) {
            ThreadUtils.SERVICE.execute(new Producer());
        }
        ThreadUtils.SERVICE.shutdown();
    }
}
