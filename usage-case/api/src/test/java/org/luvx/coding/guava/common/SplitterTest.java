package org.luvx.coding.guava.common;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;

import org.junit.jupiter.api.Test;

public class SplitterTest {
    @Test
    public void method2() {
        String s1 = "100,";

        String[] split = s1.split(",");
        System.out.println(Arrays.toString(split));
        List<String> strings = Splitter.on(",")
                .splitToList(s1);
        System.out.println(strings);

        String s = "红包券:100.00,分贝币:100.00,优惠券:100.00";
        Map<String, String> splitMap = Splitter.on(",")
                .withKeyValueSeparator(":")
                .split(s);
        System.out.println(splitMap);
    }
}
