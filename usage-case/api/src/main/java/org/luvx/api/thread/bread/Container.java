package org.luvx.api.thread.bread;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 装产品的容器
 * 其他实现方案: 阻塞队列
 */
public class Container<T> {
    @Getter
    private final int     capacity;
    @Getter
    private final List<T> list;

    public Container(int capacity) {
        this.capacity = capacity;
        list = new ArrayList<>(capacity);
    }

    public synchronized int size() {
        return list.size();
    }

    public synchronized boolean add(T product) {
        if (size() < capacity) {
            return list.add(product);
        }
        return false;
    }

    public synchronized boolean isFull() {
        if (size() >= capacity) {
            return true;
        }
        return false;
    }

    public synchronized boolean isEmpty() {
        return list.isEmpty();
    }

    public synchronized T get() {
        if (size() > 0) {
            return list.remove(0);
        }
        return null;
    }
}