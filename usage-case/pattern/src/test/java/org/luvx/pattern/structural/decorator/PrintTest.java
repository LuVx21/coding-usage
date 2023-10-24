package org.luvx.pattern.structural.decorator;

import org.junit.jupiter.api.Test;

public class PrintTest {
    /**
     * 被装饰一次
     */
    @Test
    public void printTest() {
        LogPrinter logPrinter = new LogPrinter();
        LogPrinterDecorator decorator = new LogPrinterDecorator(logPrinter);
        decorator.printLog();
    }

    /**
     * 被装饰2次
     */
    @Test
    public void printTest1() {
        LogPrinter logPrinter = new LogPrinter();
        LogPrinterDecorator decorator = new LogPrinterDecorator(logPrinter);
        LogPrinterDecorator1 decorator1 = new LogPrinterDecorator1(decorator);
        decorator1.printLog();
    }
}
