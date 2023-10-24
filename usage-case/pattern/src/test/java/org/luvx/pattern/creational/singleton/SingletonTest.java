package org.luvx.pattern.creational.singleton;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class SingletonTest {

    @Test
    public void createTest() {
        Singleton2 instance = Singleton2.getInstance();
        Singleton2 instance1 = Singleton2.getInstance();
        log.info("相同:{}", instance == instance1);
    }

    @Test
    public void createTest1() {
        Singleton1 instance = Singleton1.getInstance();
        Singleton1 instance1 = Singleton1.getInstance();
        log.info("相同:{}", instance == instance1);
    }

    @Test
    public void createTest3() {
        log.info("相同:{}", Singleton3.INSTANCE == Singleton3.INSTANCE);
    }

}
