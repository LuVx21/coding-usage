package org.luvx.collections.queue;

import org.luvx.common.util.PrintUtils;

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
        PrintUtils.println(heap.poll(), heap.poll());
    }
}
