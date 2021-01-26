package org.luvx.algorithm.queue;

/**
 * @ClassName: org.luvx.algorithm.queue
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/8/2 17:19
 */
public class BlockQueueTest {

    public static void main(String[] args) throws Exception {
        final BlockQueueSync myQueue = new BlockQueueSync();
        // final BlockQueue myQueue = new BlockQueue();
        initMyQueue(myQueue);

        Thread t1 = new Thread(() -> {
            myQueue.put("h");
            myQueue.put("i");
        }, "t1");

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
                myQueue.get();
                Thread.sleep(2000);
                myQueue.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2");

        t1.start();
        Thread.sleep(1000);
        t2.start();
    }

    private static void initMyQueue(
            BlockQueueSync
                    // BlockQueue
                    myQueue) {
        myQueue.put("a");
        myQueue.put("b");
        myQueue.put("c");
        myQueue.put("d");
        myQueue.put("e");
        System.out.println("当前元素个数：" + myQueue.size());
    }
}
