package org.luvx.pattern.creational.builder.a;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.luvx.pattern.creational.builder.Car;

@Slf4j
public class BuilderTest {

    @Test
    public void createTest() {
        Builder builder = new CarBuilder();
        Director director = new Director(builder);
        Car car = director.build();
        log.info("car:{}", car);
    }

    /**
     * 实际相当于指定了两次建造者,而且是同一个建造者对象
     */
    @Test
    public void createTest1() {
        Builder builder = new CarBuilder();
        Director director = new Director(builder);
        Car car = director.build1(builder);
        log.info("car:{}", car);
    }

    @Test
    public void createTest2() {
        Builder builder = new CarBuilder();
        Director director = new Director();
        director.setBuilder(builder);
        director.build();
    }

    @Test
    public void createTest3() {
        Builder builder = new CarBuilder();
        Director director = new Director();
        director.build1(builder);
    }
}