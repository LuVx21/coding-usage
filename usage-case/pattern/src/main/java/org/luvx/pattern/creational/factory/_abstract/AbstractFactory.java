package org.luvx.pattern.creational.factory._abstract;

import org.luvx.pattern.creational.factory.bean.ProductA;
import org.luvx.pattern.creational.factory.bean.ProductB;

public abstract class AbstractFactory {
    /**
     * 工厂方法一
     *
     * @return
     */
    public abstract ProductA getProductA();

    /**
     * 工厂方法二
     *
     * @return
     */
    public abstract ProductB getProductB();
}
