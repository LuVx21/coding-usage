package org.luvx.coding.collection;

import org.junit.jupiter.api.Test;
import org.luvx.coding.common.more.MorePrints;

import java.util.ArrayDeque;

public class DequeCase {
    ArrayDeque<String> deque = new ArrayDeque<>();
    // LinkedList<String> deque = new LinkedList<>();

    /**
     * 针对first操作
     */
    @Test
    void stackTest() {
        deque.push("a");
        deque.push("b");
        MorePrints.println(deque.peek(), deque.pop(), deque.peek());
    }

    @Test
    void queueTest() {
        deque.offer("a");
        deque.offer("b");
        MorePrints.println(deque.peek(), deque.poll(), deque.peek());
    }

    /**
     * 双端都可操作读写
     */
    @Test
    void dequeTest() {
    }
}
