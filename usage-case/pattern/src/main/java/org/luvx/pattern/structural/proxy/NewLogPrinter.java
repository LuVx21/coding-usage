package org.luvx.pattern.structural.proxy;

import lombok.extern.slf4j.Slf4j;

/**
 * Cglib动态代理时,可以不实现接口
 */
@Slf4j
public class NewLogPrinter {
    public void printLog() {
        log.info("打印Log...");
    }

    /**
     * Cglib代理用
     * static/final的方法不会被增强
     */
    public final void pringlogNo() {
        log.info("不加强功能的打印Log...");
    }
}
