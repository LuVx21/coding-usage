package com.carrotsearch.hppc;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class Main {
    @Test
    void m1() {
        // IntSet set = new IntHashSet();
        // IntStack stack = new IntStack();
        // IntArrayDeque deque = new IntArrayDeque();
    }

    @Test
    void m2() {
        IntArrayList list = new IntArrayList();
        list.add(10);
        System.out.println(list);
    }

    @Test
    void m3() {
        IntObjectHashMap<String> map = new IntObjectHashMap<>();
        map.put(10, "foobar");
        Assertions.assertEquals("foobar", map.get(10));
        System.out.println(map);
    }

    @Test
    void m4() {
    }

    @Test
    void m5() {
    }
}
