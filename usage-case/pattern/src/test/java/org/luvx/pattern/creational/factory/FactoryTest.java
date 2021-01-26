package org.luvx.pattern.creational.factory;

import org.junit.Test;
import org.luvx.pattern.creational.factory._abstract.FactoryAB;
import org.luvx.pattern.creational.factory.factory.ProductFactoryA;
import org.luvx.pattern.creational.factory.factory.ProductFactoryB;
import org.luvx.pattern.creational.factory.simple.Factory;

public class FactoryTest {

    /**
     * 简单方法模式
     */
    @Test
    public void getProductTest() {
        Factory factory = new Factory();
        factory.getProduct("A").methodDiff();
        factory.getProduct("B").methodDiff();
    }

    /**
     * 工厂方法模式
     */
    @Test
    public void getProductTest1() {
        ProductFactoryA factoryA = new ProductFactoryA();
        ProductFactoryB factoryB = new ProductFactoryB();
        factoryA.getProduct().methodDiff();
        factoryB.getProduct().methodDiff();
    }

    /**
     * 抽象工厂方法模式
     */
    @Test
    public void getProductTest2() {
        FactoryAB factoryAB = new FactoryAB();
        factoryAB.getProductA().methodDiff();
        factoryAB.getProductB().methodDiff();
    }
}
