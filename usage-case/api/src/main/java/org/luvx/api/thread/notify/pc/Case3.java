package org.luvx.api.thread.notify.pc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Case3 {
    private static volatile Integer count = 0;

    final BlockingQueue queue = new ArrayBlockingQueue<>(10);

    class Producer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                Thread.sleep(3000);
                queue.put(1);
                log.info("生产者({})生产, {} -> {}", Thread.currentThread().getName(), count, ++count);
            }
        }
    }

    class Consumer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                TimeUnit.SECONDS.sleep(3);
                queue.take();
                log.info("消费者({})消费, {} -> {}", Thread.currentThread().getName(), count, --count);
            }
        }
    }
}
