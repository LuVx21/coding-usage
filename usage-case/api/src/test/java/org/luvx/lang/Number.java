package org.luvx.lang;

import java.math.BigDecimal;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.luvx.coding.common.more.MorePrints;

public class Number {

    @Test
    void m1() {
        BigDecimal amount = new BigDecimal("100.");
        System.out.println(amount);
        System.out.println(amount.scale());
        // amount.add()
    }
}
