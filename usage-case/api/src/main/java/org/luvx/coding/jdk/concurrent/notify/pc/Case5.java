package org.luvx.concurrent.notify.pc;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author: Ren, Xie
 */
@Slf4j
public class Case5 {
    final PipedInputStream  pis = new PipedInputStream();
    final PipedOutputStream pos = new PipedOutputStream();

    {
        try {
            pis.connect(pos);
        } catch (IOException e) {
            e.printStackTrace();
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
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    pos.close();
                    pis.close();
                } catch (IOException e) {
                    e.printStackTrace();
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
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    pos.close();
                    pis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
