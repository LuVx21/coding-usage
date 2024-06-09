package org.luvx.coding.jdk.concurrent.notify.pc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.luvx.coding.jdk.concurrent.utils.ThreadUtils;

import java.util.concurrent.Semaphore;

@Slf4j
public class PCBySemaphore {
    private static volatile Integer count = 0;

    final Semaphore notFull  = new Semaphore(10);
    final Semaphore notEmpty = new Semaphore(0);
    final Semaphore mutex    = new Semaphore(1);

    class Producer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                try {
                    notFull.acquire();
                    mutex.acquire();
                    log.info("生产者({})生产, {} -> {}", Thread.currentThread().getName(), count, ++count);
                } catch (InterruptedException _) {
                } finally {
                    mutex.release();
                    notEmpty.release();
                }
                Thread.sleep(3000);
            }
        }
    }

    class Consumer implements Runnable {
        @SneakyThrows
        @Override
        public void run() {
            while (true) {
                try {
                    notEmpty.acquire();
                    mutex.acquire();
                    log.info("消费者({})消费, {} -> {}", Thread.currentThread().getName(), count, --count);
                } catch (InterruptedException _) {
                } finally {
                    mutex.release();
                    notFull.release();
                }
                Thread.sleep(3000);
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
