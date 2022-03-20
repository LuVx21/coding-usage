package org.luvx.pattern.structural.proxy.dynamic;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.luvx.pattern.structural.proxy.LogPrinter;
import org.luvx.pattern.structural.proxy.Printable;

import com.google.common.io.Files;

import lombok.SneakyThrows;

public class LogPrinterHandlerTest {

    /**
     * jdk动态代理
     */
    @Test
    void run01() {
        // 让代理对象的class文件写入到磁盘, 在 junit 中无效, 在 main 方法中应该有效但不似乎仍是无效
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Printable logPrinter = (Printable) new LogPrinterHandler(new LogPrinter()).newProxyInstance();
        logPrinter.printLog();
    }

    /**
     * 输出代理类的内容
     * 17 版本jdk 下出现的问题:
     * module java.base does not “opens java.lang.reflect“ to unnamed module
     * --add-opens java.base/java.lang.reflect=ALL-UNNAMED
     */
    @SneakyThrows
    @Test
    void m1() {
        Class<?> clazz = Class.forName("java.lang.reflect.ProxyGenerator");
        Method method = clazz.getDeclaredMethod("generateProxyClass",
                ClassLoader.class, String.class, List.class, Integer.TYPE);
        Object[] args = {LogPrinter.class.getClassLoader(),
                "$Proxy0",
                Arrays.asList(LogPrinter.class.getInterfaces()),
                Modifier.PUBLIC};
        method.setAccessible(true);
        Object invoke = method.invoke(null, args);
        if (invoke instanceof byte[]) {
            Files.write((byte[]) invoke, new File(
                    getClass().getResource("").getPath() + "$Proxy0.class")
            );
        }
        // System.out.println(constructors.length);
    }
}
