package org.luvx.algorithm.linkarray;

import lombok.Getter;
import lombok.Setter;

/**
 * 链表节点类
 */
@Getter
@Setter
public class Node {
    Node pre;
    Node next;
    int data;

    Node() {
    }

    Node(Node pre, Node next, int data) {
        this.pre = pre;
        this.next = next;
        this.data = data;
    }

    public boolean isHead() {
        return pre == null;
    }

    public boolean isTail() {
        return next == null;
    }
}