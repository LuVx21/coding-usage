package org.luvx.coding.guava;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class BloomFilterCaseTest {
    @Test
    void m1() {
        // 预计插入10亿个元素，误判率为0.01
        BloomFilter<CharSequence> bloomFilter = BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8), 10_0000_0000L, 0.01
        );
        for (int i = 1; i <= 1_0000; i++) {
            bloomFilter.put("key_" + i);
        }

        // 判断key_9999是否存在
        if (bloomFilter.mightContain("key_9999")) {
            System.out.println("key_9999 存在");
        } else {
            System.out.println("key_9999 不存在");
        }

        // 判断key_10000是否存在
        if (bloomFilter.mightContain("key_10000")) {
            System.out.println("key_10000 存在");
        } else {
            System.out.println("key_10000 不存在");
        }
    }

    @Test
    void m2() {
    }
}
