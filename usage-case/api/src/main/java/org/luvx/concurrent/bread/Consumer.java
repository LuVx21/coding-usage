package org.luvx.concurrent.bread;

import lombok.extern.slf4j.Slf4j;

import lombok.extern.slf4j.Slf4j;

/**
 * 消费者
 */
@Slf4j
public class Consumer implements Runnable {
    private final Object           producerMonitor;
    private final Object           consumerMonitor;
    private final Container<Bread> container;

    public Consumer(Object producerMonitor, Object consumerMonitor, Container<Bread> container) {
        this.producerMonitor = producerMonitor;
        this.consumerMonitor = consumerMonitor;
        this.container = container;
    }

    @Override
    public void run() {
        while (true) {
            consume();
        }
    }

    public void consume() {
        if (container.isEmpty()) {
            synchronized (producerMonitor) {
                if (container.isEmpty()) {
                    producerMonitor.notify();
                }
            }
            synchronized (consumerMonitor) {
                try {
                    if (container.isEmpty()) {
                        consumerMonitor.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Bread bread = container.get();
            log.info("消费者({})消费, {} -> {}", Thread.currentThread().getName(), container.size() + 1, container.size());
        }
    }
}