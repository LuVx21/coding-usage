package org.luvx.algorithm.tree;

import lombok.Getter;
import lombok.Setter;

/**
 * 二叉树节点类
 *
 * @param <T>
 */
@Getter
@Setter
public class Node<T> {

    private Node<T> left;
    private Node<T> right;
    private T       data;

    Node(T data) {
        this.data = data;
    }

    public boolean isLeaf() {
        return this.getLeft() == null && this.getRight() == null;
    }
}
