package org.luvx.pattern.structural.proxy._static;

import org.junit.jupiter.api.Test;

public class ProxyTest {
    /**
     * 静态代理
     * 标准的使用结构
     */
    @Test
    public void run00() {
        new PrinterStaticProxy().printLog();
    }

    /**
     * getPrinterProxy设置为public
     * 可以动态设置增强的方法
     */
    @Test
    public void run01() {
        String methodName = "printLog";
        new PrinterStaticProxy().getPrinterProxy(methodName).printLog();
    }

    /**
     * 不增强方法
     */
    @Test
    public void run02() {
        String methodName = "printLogNo";
        new PrinterStaticProxy().getPrinterProxy(methodName).printLog();
    }

}
