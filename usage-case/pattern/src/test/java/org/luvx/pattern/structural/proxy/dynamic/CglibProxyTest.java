package org.luvx.pattern.structural.proxy.dynamic;

import org.junit.Test;
import org.luvx.pattern.structural.proxy.LogPrinter;
import org.luvx.pattern.structural.proxy.NewLogPrinter;

public class CglibProxyTest {

    /**
     * Cglib动态代理: 代理非接口实现类
     */
    @Test
    public void run01() {
        NewLogPrinter logPrinter = (NewLogPrinter) new CglibProxy().newProxyInstance(new NewLogPrinter());
        logPrinter.printLog();
        logPrinter.pringlogNo();
    }

    /**
     * Cglib动态代理: 代理接口实现类
     */
    @Test
    public void run02() {
        LogPrinter logPrinter = (LogPrinter) new CglibProxy().newProxyInstance(new LogPrinter());
        logPrinter.printLog();
    }
}
