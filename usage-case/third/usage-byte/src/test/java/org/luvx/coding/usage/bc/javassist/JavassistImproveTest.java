package org.luvx.coding.usage.bc.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class JavassistImproveTest {
    static String className  = "org.luvx.coding.usage.bc.javassist.JavassistImproveTest$Task";
    static String methodName = "exec";

    /**
     * <pre>
     *     运行时需要添加jvm参数
     *     --add-opens java.base/java.lang=ALL-UNNAMED
     * </pre>
     */
    @Test
    void m1() throws Exception {
        CtClass cc = ClassPool.getDefault().get(className);
        CtMethod method = cc.getDeclaredMethod(methodName);

        String a = "long start = System.currentTimeMillis();";
        String b = "System.out.println(\"耗时: \" + (System.currentTimeMillis() - start) + \"ms\");";

        // 1: 行不通的方式
        // method.insertBefore(a);
        // method.insertAfter(b);

        // 2: 可行
        String newName = methodName + "Impl";
        method.setName(newName);
        CtMethod newMethod = CtNewMethod.make("public void " + methodName + "() {"
                        + a
                        + newName + "();"
                        + b
                        + "}"
                , cc);
        cc.addMethod(newMethod);

        Task task = (Task) cc.toClass().getConstructor().newInstance();
        task.exec();
    }

    @Test
    void m2() throws Exception {
        ProxyFactory factory = new ProxyFactory();
        factory.setSuperclass(Task.class);
        factory.setFilter(m -> Objects.equals(methodName, m.getName()));

        Class<?> c = factory.createClass();
        Proxy proxy = (Proxy) c.getConstructor().newInstance();
        proxy.setHandler(
                (self, m, proceed, args) -> {
                    long start = System.currentTimeMillis();
                    Object result = proceed.invoke(self, args);
                    System.out.println("耗时:" + (System.currentTimeMillis() - start) + "ms");
                    return result;
                }
        );

        Task task = (Task) proxy;
        task.exec();
    }

    @NoArgsConstructor
    static class Task {
        public void exec() {
            try {
                Thread.sleep(1_000L);
            } catch (Exception ignore) {
            }
        }
    }
}