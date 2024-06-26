package org_.apache.commons.collections4.bag;

import org.apache.commons.collections4.bag.HashBag;
import org.junit.jupiter.api.Test;

class BagTest {
    @Test
    void m1() {
        var bag = new HashBag<Integer>();
        bag.add(1);
        bag.add(2);
        bag.add(3);
        bag.add(2);
        System.out.println(bag.getCount(2));
        System.out.println(bag);
    }
}
