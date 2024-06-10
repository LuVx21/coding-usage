package org.luvx.coding.jdk.concurrent.countdown;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.luvx.coding.common.concurrent.Threads;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Exchanger;

@Slf4j
public class ExchangerCase1 {
    private static final int SIZE = 5;
    Exchanger<BlockingQueue<Integer>> exchanger = new Exchanger<>();

    BlockingQueue<Integer> initialEmptyBuffer = new ArrayBlockingQueue<>(SIZE);
    BlockingQueue<Integer> initialFullBuffer  = new ArrayBlockingQueue<>(SIZE);

    class FillingLoop implements Runnable {
        @SneakyThrows
        public void run() {
            int i = 1;
            while (true) {
                var a = initialEmptyBuffer.offer(i++);
                if (initialEmptyBuffer.size() == SIZE) {
                    log.info("{} 已满 初始空:{} 初始满:{}", Thread.currentThread().getName(), initialEmptyBuffer, initialFullBuffer);
                    initialEmptyBuffer = exchanger.exchange(initialEmptyBuffer);
                }
                Threads.sleep(2_000);
            }
        }
    }

    class EmptyingLoop implements Runnable {
        @SneakyThrows
        public void run() {
            while (true) {
                Integer i = initialFullBuffer.poll();
                if (i != null) {
                    log.info("{} {} 初始空:{} 初始满:{}", Thread.currentThread().getName(), i, initialEmptyBuffer, initialFullBuffer);
                }
                if (initialFullBuffer.isEmpty()) {
                    initialFullBuffer = exchanger.exchange(initialFullBuffer);
                }
                Threads.sleep(2_000);
            }
        }
    }

    void main() {
        for (int i = 0; i < SIZE; i++) {
            initialFullBuffer.offer(i + 1001);
        }

        new Thread(new FillingLoop(), "线程1").start();
        new Thread(new EmptyingLoop(), "线程2").start();
    }
}
