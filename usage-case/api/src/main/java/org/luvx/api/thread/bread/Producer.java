package org.luvx.api.thread.bread;

/**
 * 生产者
 */
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
        Bread bread = step2();
        if (container.isFull()) {
            synchronized (consumerMonitor) {
                if (container.isFull()) {
                    consumerMonitor.notify();
                }
            }
            synchronized (producerMonitor) {
                try {
                    if (container.isFull()) {
                        System.out.println("生产者挂起...");
                        producerMonitor.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            boolean result = container.add(bread);
            System.out.println("Producer:" + result);
        }
    }

    public void step1() {
    }

    public Bread step2() {
        return new Bread();
    }
}