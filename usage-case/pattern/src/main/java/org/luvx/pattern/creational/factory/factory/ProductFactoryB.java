package org.luvx.pattern.creational.factory.factory;

import org.luvx.pattern.creational.factory.bean.FactoryInter;
import org.luvx.pattern.creational.factory.bean.Product;
import org.luvx.pattern.creational.factory.bean.ProductB;

/**
 * 工厂方法模式
 * <p>
 * 为解决简单工厂模式中工厂类的臃肿问题,每种产品都由一个工厂创建
 */
public class ProductFactoryB implements FactoryInter {
    @Override
    public Product getProduct() {
        return new ProductB();
    }
}
