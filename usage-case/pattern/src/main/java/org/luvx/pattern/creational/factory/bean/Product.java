package org.luvx.pattern.creational.factory.bean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Product {
    //所有产品类的公共业务方法
    public void methodSame() {
        //公共方法的实现
        log.info("父类的公共方法");
    }

    //声明抽象业务方法
    public abstract void methodDiff();
}
