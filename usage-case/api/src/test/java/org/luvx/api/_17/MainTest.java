package org.luvx.api._17;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

import org.junit.jupiter.api.Test;

class MainTest {
    /**
     * 引入RandomGenerator及RandomGeneratorFactory提供更好的随机数生成
     */
    @Test
    public void m1() {
        RandomGenerator generator = RandomGeneratorFactory.all()
                .filter(RandomGeneratorFactory::isJumpable)
                .filter(factory -> factory.stateBits() > 128)
                .findAny()
                .map(RandomGeneratorFactory::create)
                //  .map(JumpableGenerator.class::cast)
                .orElseThrow();
    }

    //     String formatterPatternSwitch(Object o) {
    //        return switch (o) {
    //            case Integer i -> String.format("int %d", i);
    //            case Long l    -> String.format("long %d", l);
    //            case Double d  -> String.format("double %f", d);
    //            case String s  -> String.format("String %s", s);
    //            default        -> o.toString();
    //        };
    //    }
}