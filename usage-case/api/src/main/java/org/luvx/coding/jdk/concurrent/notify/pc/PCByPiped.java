package org.luvx.coding.jdk.concurrent.notify.pc;

import lombok.extern.slf4j.Slf4j;
import org.luvx.coding.jdk.concurrent.utils.ThreadUtils;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Slf4j
public class PCByPiped {
    final PipedInputStream  pis = new PipedInputStream();
    final PipedOutputStream pos = new PipedOutputStream();

    {
        try {
            pis.connect(pos);
        } catch (IOException _) {
        }
    }

    class Producer implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    TimeUnit.SECONDS.sleep(1);
                    int num = (int) (Math.random() * 255);
                    log.info("生产者({})生产 -> {}", Thread.currentThread().getName(), num);
                    pos.write(num);
                    pos.flush();
                }
            } catch (Exception _) {
            } finally {
                try {
                    pos.close();
                    pis.close();
                } catch (IOException _) {
                }
            }
        }
    }

    class Consumer implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    TimeUnit.SECONDS.sleep(1);
                    int num = pis.read();
                    log.info("消费者({})消费 -> {}", Thread.currentThread().getName(), num);
                }
            } catch (Exception _) {
            } finally {
                try {
                    pos.close();
                    pis.close();
                } catch (IOException _) {
                }
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
