package org.luvx.pattern.structural.proxy.dynamic;

import org.junit.Test;
import org.luvx.pattern.structural.proxy.LogPrinter;
import org.luvx.pattern.structural.proxy.Printable;

public class LogPrinterHandlerTest {

    /**
     * jdk动态代理
     */
    @Test
    public void run01() {
        Printable logPrinter = (Printable) new LogPrinterHandler().newProxyInstance(new LogPrinter());
        logPrinter.printLog();
    }
}
