package org.luvx.coding.jdk.reflect;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class MethodTest {

    @Test
    public void run01() {
        Class clazz = InnerClass.class;

        Method[] methods = clazz.getDeclaredMethods();

        for (Method method : methods) {
            System.out.println(method);
        }

    }
}
