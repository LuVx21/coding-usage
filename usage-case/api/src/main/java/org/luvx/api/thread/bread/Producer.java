package org.luvx.api.thread.bread;

import lombok.extern.slf4j.Slf4j;

/**
 * 生产者
 */
@Slf4j
public class Producer implements Runnable {
    private final Object           producerMonitor;
    private final Object           consumerMonitor;
    private final Container<Bread> container;

    public Producer(Object producerMonitor, Object consumerMonitor, Container<Bread> container) {
        this.producerMonitor = producerMonitor;
        this.consumerMonitor = consumerMonitor;
        this.container = container;
    }

    @Override
    public void run() {
        while (true) {
            produce();
        }
    }

    public void produce() {
        step1();
        if (container.isFull()) {
            synchronized (consumerMonitor) {
                if (container.isFull()) {
                    consumerMonitor.notify();
                }
            }
            synchronized (producerMonitor) {
                try {
                    if (container.isFull()) {
                        producerMonitor.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Bread bread = step2();
            boolean result = container.add(bread);
            log.info("生产者({})生产, {} -> {}", Thread.currentThread().getName(), container.size() - 1, container.size());
        }
    }

    public void step1() {
    }

    public Bread step2() {
        return new Bread();
    }
}