package org.luvx.pattern.structural.decorator;

import lombok.extern.slf4j.Slf4j;

/**
 * 具体装饰器
 * 用于装饰具体被装饰者
 */
@Slf4j
public class LogPrinterDecorator extends PrinterDecorator {
    public LogPrinterDecorator(Printer printer) {
        super(printer);
    }

    public void beforePrint() {
        log.info("打印前操作...");
    }

    public void afterPrint() {
        log.info("打印后操作...");
    }

    @Override
    public void printLog() {
        beforePrint();
        super.printLog();
        afterPrint();
    }
}
