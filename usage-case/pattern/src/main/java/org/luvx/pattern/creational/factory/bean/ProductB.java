package org.luvx.pattern.creational.factory.bean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductB extends Product {
    @Override
    public void methodDiff() {
        log.info("产品B");
    }
}