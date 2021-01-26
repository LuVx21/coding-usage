package org.luvx.pattern.creational.factory.simple;

import org.luvx.pattern.creational.factory.bean.Product;
import org.luvx.pattern.creational.factory.bean.ProductA;
import org.luvx.pattern.creational.factory.bean.ProductB;

/**
 * 简单工厂模式,也被称为静态工厂方法模式
 * <p>
 * 新增一个方法时,需要创建一个产品类继承Product类,并实现抽象方法
 * 当产品数量很多的情况下,下面getProduct方法很臃肿,条件语句很多
 */
public class Factory {
    /**
     * 静态工厂方法
     *
     * @param arg
     * @return
     */
    public static Product getProduct(String arg) {
        Product product = null;
        if (arg.equalsIgnoreCase("A")) {
            product = new ProductA();
        } else if (arg.equalsIgnoreCase("B")) {
            product = new ProductB();
        }
        return product;
    }
}
