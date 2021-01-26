package org.luvx.api.thread.bread;

/**
 * 消费者
 */
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
                        System.out.println("消费者挂起...");
                        consumerMonitor.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Bread bread = container.get();
            System.out.println("bread:" + bread);
        }
    }
}