package org.luvx.retry;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一次性 HASH
 *
 * @author Ren, Xie
 */
public class ConsistentHash<T> {
    @Getter
    @Setter
    private       int                   virtualNodeCount = 1;
    private final SortedMap<Integer, T> hashRing         = new TreeMap<>();

    public ConsistentHash() {
    }

    public ConsistentHash(int virtualNodeCount) {
        this.virtualNodeCount = virtualNodeCount;
    }

    public synchronized void add(T node) {
        for (int i = 0; i < virtualNodeCount; i++) {
            addNode(node, i);
        }
    }

    public synchronized void add(Collection<T> nodes) {
        int i = 0;
        for (T node : nodes) {
            addNode(node, ++i);
        }
    }

    private void addNode(T node, int i) {
        hashRing.put(getHashKey(node, i), node);
    }

    public synchronized void remove(T node) {
        for (int i = 0; i < virtualNodeCount; i++) {
            hashRing.remove(getHashKey(node, i));
        }
    }

    public int getHashKey(T node, int i) {
        final String key = "HASH-" + node.toString() + "-NODE-" + i;
        return hash(key);
    }

    public T getNode(T key) {
        SortedMap<Integer, T> subMap = hashRing.tailMap(hash(key.toString()));
        if (subMap.isEmpty()) {
            return hashRing.get(hashRing.firstKey());
        }
        return subMap.get(subMap.firstKey());
    }

    /**
     * FNV1_32_HASH 算法计算 Hash 值
     *
     * @param key 待计算 KEY
     * @return hash值
     */
    private int hash(final String key) {
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash ^ key.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;

        // 取正数
        return hash < 0 ? Math.abs(hash) : hash;
    }
}