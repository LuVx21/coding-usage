package org.luvx.coding.jdk.lang.utils;

import org.luvx.coding.common.more.MorePrints;

import java.util.BitSet;

public class BitSetCase {
    public static void main(String[] args) {
        BitSet bs = new BitSet(50);
        bs.set(0);
        MorePrints.println(bs.get(0), bs.get(100));
    }
}
