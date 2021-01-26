package org.luvx.pattern.structural.decorator;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体被装饰者
 * 和装饰器接口实现同一个接口
 */
@Slf4j
public class LogPrinter implements Printer {
    @Override
    public void printLog() {
        log.info("打印Log");
    }
}
