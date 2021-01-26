package org.luvx.pattern.structural.decorator;

/**
 * 装饰器,和具体被装饰者实现共同接口
 */
public abstract class PrinterDecorator implements Printer {
    private Printer printer;

    public PrinterDecorator(Printer printer) {
        this.printer = printer;
    }

    @Override
    public void printLog() {
        printer.printLog();
    }
}
