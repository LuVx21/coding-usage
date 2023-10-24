package org.luvx.collections.queue;

import org.luvx.coding.common.more.MorePrints;

import java.util.Comparator;
import java.util.PriorityQueue;

public class QueueCase {

    public void m1() {
        // 最小,最大堆
        Comparator<Integer> compare = Integer::compare;
        PriorityQueue<Integer> heap = new PriorityQueue<>(compare.reversed());
        heap.offer(3);
        heap.offer(1);
        heap.offer(2);
        MorePrints.println(heap.poll(), heap.poll());
    }
}
