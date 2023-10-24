package org.luvx.coding.hppc;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntHashSet;

import org.junit.jupiter.api.Test;

class HppcTest {
    @Test
    void m1() {
        IntArrayList list = IntArrayList.from(1, 2, 3);
        System.out.println(list);

        IntHashSet set = IntHashSet.from(1, 2, 3, 4, 3);
        System.out.println(set);
        System.out.println(set.contains(2));
    }
}
