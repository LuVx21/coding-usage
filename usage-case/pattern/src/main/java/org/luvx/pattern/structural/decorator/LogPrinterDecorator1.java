package org.luvx.pattern.structural.decorator;

import lombok.extern.slf4j.Slf4j;

/**
 * 另一个装饰器
 * 用于配合其他装饰器多次装饰同一对象
 */
@Slf4j
public class LogPrinterDecorator1 extends PrinterDecorator {
    public LogPrinterDecorator1(Printer printer) {
        super(printer);
    }

    public void beforePrint1() {
        log.info("打印前另一个操作");
    }

    public void afterPrint1() {
        log.info("打印后另一个操作");
    }

    @Override
    public void printLog() {
        beforePrint1();
        super.printLog();
        afterPrint1();
    }
}
