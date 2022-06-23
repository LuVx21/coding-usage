package org.luvx.lang;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.luvx.common.more.MorePrints;

import java.util.Random;

/**
 * 获取随机数的方式
 */
public class RandomNum {

    @Test
    void getRandom() {
        int start = 0, end = 10;
        int bound = end - start + 1;

        // [0.0 , 1.0)
        double num = Math.random();
        int i1 = (int) (num * bound) + start;

        Random random = new Random();
        int i2 = random.nextInt(bound) + start;
        MorePrints.println(i1, i2);
    }

    @Test
    void getRandom1() {
        RandomUtils.nextInt(0, 10);
    }
}
