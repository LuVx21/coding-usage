package org.luvx.pattern.structural.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.luvx.pattern.structural.proxy.Aspect;
import org.luvx.pattern.structural.proxy.LogPrinter;
import org.luvx.pattern.structural.proxy.Printable;

import lombok.AllArgsConstructor;

/**
 * 动态代理
 * 动态代理类只能代理接口(不支持抽象类)
 */
@AllArgsConstructor
public class LogPrinterHandler implements InvocationHandler {
    private final Printable targetObject;

    public Object newProxyInstance() {
        Class<? extends Printable> clazz = targetObject.getClass();
        return Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 对所有方法都进行加强,则没有此if条件
        if ("printLog".equals(method.getName())) {
            Aspect.printBefore();
            Object obj = method.invoke(targetObject, args);
            Aspect.printAfter();
            return obj;
        }
        return method.invoke(targetObject, args);
    }
}
