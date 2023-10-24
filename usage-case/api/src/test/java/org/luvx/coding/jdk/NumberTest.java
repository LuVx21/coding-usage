package org.luvx.coding.jdk;

import java.math.BigDecimal;
import java.util.Map;

import com.google.common.base.Preconditions;

import org.apache.commons.collections4.MapUtils;
import org.junit.jupiter.api.Test;

public class NumberTest {

    @Test
    void m1() {
        BigDecimal amount = new BigDecimal("100.");
        System.out.println(amount);
        System.out.println(amount.scale());
        // amount.add()
    }

    @Test
    void m2() {
        Map<String, Double> map = Map.of("payPrice", 100.1);
        String payPrice1 = MapUtils.getString(map, "payPrice");
        System.out.println(payPrice1);
        BigDecimal bigDecimal = new BigDecimal(payPrice1);
        System.out.println(bigDecimal);

        // BigDecimal bigDecimal1 = new BigDecimal("");
        // System.out.println(bigDecimal1);
    }

    @Test
    void m3() {
        BigDecimal payPrice = new BigDecimal("10.1");
        BigDecimal multiply = payPrice.multiply(new BigDecimal("1.5"));
        System.out.println(multiply);

        Preconditions.checkArgument(payPrice.compareTo(multiply) > 0,
                "非法参数->支付金额(%s)需大于0且小于等于%s(%s x 1.15)", payPrice, multiply, payPrice
        );
    }
}
