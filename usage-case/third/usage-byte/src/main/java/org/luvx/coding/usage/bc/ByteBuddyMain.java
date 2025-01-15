package org.luvx.coding.usage.bc;

import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.luvx.coding.common.more.MorePrints;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static net.bytebuddy.matcher.ElementMatchers.named;

public class ByteBuddyMain {
    @SneakyThrows
    void main() {
        // m0();
        m1();
        // m2();
    }

    private void m2() throws Exception {
        ByteBuddyAgent.install();
        new ByteBuddy()
                .redefine(Foo.class)
                .method(named("sayHelloFoo"))
                .intercept(FixedValue.value("Hello Foo Redefined"))
                .make()
                .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

        Foo f = new Foo();

        MorePrints.println(f.sayHelloFoo());
    }

    private void m1() throws Exception {
        Class<?> clazz = new ByteBuddy()
                .subclass(Object.class)
                .name("org.luvx.coding.usage.bc.M2")
                .defineField("x", String.class, Modifier.PUBLIC)
                .defineMethod("custom", String.class, Modifier.PUBLIC)
                .intercept(FixedValue.value("new method custom()"))
                // .intercept(MethodDelegation.to(Bar.class))
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        Object o = clazz.getConstructor().newInstance();
        Field field = clazz.getDeclaredField("x");
        field.set(o, "123");
        Method m = clazz.getDeclaredMethod("custom", null);

        MorePrints.println(field.get(o), m.invoke(o));
    }

    private void m0() throws Exception {
        try (DynamicType.Unloaded<Object> unloadedType = new ByteBuddy()
                .subclass(Object.class)
                .name("org.luvx.coding.usage.bc.M1")
                .method(ElementMatchers.isToString())
                .intercept(FixedValue.value("Hello World ByteBuddy!"))
                .make()) {
            Class<?> dynamicType = unloadedType.load(getClass().getClassLoader()).getLoaded();
            Object o = dynamicType.getConstructor().newInstance();
            MorePrints.println(o, dynamicType.getName());
        }
    }

    static class Foo {
        String sayHelloFoo() {
            return "Hello in Foo!";
        }
    }

    static class Bar {
        public static String sayHelloBar() {
            return "Holla in Bar!";
        }
    }
}
