package org.luvx.pattern.structural.proxy;

import lombok.extern.slf4j.Slf4j;

/**
 * 假设此类不可修改和直接访问
 * 但可以通过代理的方式进行访问
 * Cglib动态代理时,可以不实现接口
 */
@Slf4j
public class LogPrinter implements Printable {
    @Override
    public void printLog() {
        log.info("打印Log...");
    }
}
