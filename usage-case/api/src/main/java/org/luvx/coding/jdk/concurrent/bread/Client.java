package org.luvx.coding.jdk.concurrent.bread;

// http://blog.csdn.net/luohuacanyue/article/list/1
// http://blog.csdn.net/luohuacanyue/article/details/14648185
// http://blog.csdn.net/luohuacanyue/article/details/16359777

public class Client {
    public static void main(String[] args) {
        Object producerMonitor = new Object();
        Object consumerMonitor = new Object();
        Container<Bread> container = new Container<>(10);

        new Thread(new Producer(producerMonitor, consumerMonitor, container)).start();
        new Thread(new Producer(producerMonitor, consumerMonitor, container)).start();
        new Thread(new Producer(producerMonitor, consumerMonitor, container)).start();
        new Thread(new Producer(producerMonitor, consumerMonitor, container)).start();

        new Thread(new Consumer(producerMonitor, consumerMonitor, container)).start();
        new Thread(new Consumer(producerMonitor, consumerMonitor, container)).start();
    }
}  