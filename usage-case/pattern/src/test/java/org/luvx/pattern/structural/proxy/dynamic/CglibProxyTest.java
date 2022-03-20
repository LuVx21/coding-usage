package org.luvx.pattern.structural.proxy.dynamic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.luvx.pattern.structural.proxy.LogPrinter;
import org.luvx.pattern.structural.proxy.NewLogPrinter;
import org.springframework.cglib.core.DebuggingClassWriter;

public class CglibProxyTest {

    @BeforeEach
    void before() {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "");
    }

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
