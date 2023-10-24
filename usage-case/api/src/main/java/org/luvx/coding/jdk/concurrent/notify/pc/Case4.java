package org.luvx.concurrent.notify.pc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

@Slf4j
public class Case4 {
    private static volatile Integer count = 0;

    final Semaphore notFull  = new Semaphore(10);
    final Semaphore notEmpty = new Semaphore(0);
    final Semaphore mutex    = new Semaphore(1);

    class Producer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                Thread.sleep(3000);
                try {
                    notFull.acquire();
                    mutex.acquire();
                    log.info("生产者({})生产, {} -> {}", Thread.currentThread().getName(), count, ++count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                    notEmpty.release();
                }
            }
        }
    }

    class Consumer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                Thread.sleep(3000);
                try {
                    notEmpty.acquire();
                    mutex.acquire();
                    log.info("消费者({})消费, {} -> {}", Thread.currentThread().getName(), count, --count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                    notFull.release();
                }
            }
        }
    }
}
