package org.luvx.pattern.creational.builder.b;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.luvx.pattern.creational.builder.Car;

@Slf4j
public class Builder1Test {
    @Test
    public void creatTest() {
        Builder1 builder = new CarBuilder1();
        Car car = builder.build();
        log.info("car:{}", car);
    }

    @Test
    public void creatTest1() {
        Builder1 builder = new CarBuilder2();
        Car car = builder.wheels("4个轮子").engine("宝马引擎").build();
        log.info("car:{}", car);
    }
}