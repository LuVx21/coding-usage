package org.luvx.coding.jdk.math;

import org.junit.jupiter.api.Test;
import org.luvx.coding.common.more.MorePrints;

import java.math.BigInteger;

class BigIntCaseTest {

    @Test
    void m1() {
        BigInteger l = BigInteger.valueOf(Long.MAX_VALUE);
        l = l.add(BigInteger.valueOf(Long.MAX_VALUE));
        l = l.add(BigInteger.valueOf(Long.MAX_VALUE));
        MorePrints.println(l, Long.MAX_VALUE, l.bitLength());

        BigInteger ff = new BigInteger("ff", 16);
        System.out.println(ff);
    }
}
