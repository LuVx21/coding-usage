package org.luvx.coding.usage.bc.javassist;

import javassist.*;

import java.io.IOException;
import java.lang.reflect.Method;

public class ReadByteCode {

    static final String classname = "org.luvx.coding.usage.javassist.Bool";

    /**
     * <pre>
     *     运行时需要添加jvm参数
     *     --add-opens java.base/java.lang=ALL-UNNAMED
     * </pre>
     */
    public static void main(String[] args) throws Exception {
        // aa();
        update();
    }

    public static void update() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get(classname);

        CtMethod personFly = cc.getDeclaredMethod("run");
        personFly.insertBefore("System.out.println(\"前面\");");
        personFly.insertAfter("System.out.println(\"后面\");");

        // 新增一个方法
        CtMethod ctMethod = new CtMethod(CtClass.voidType, "run1", new CtClass[]{}, cc);
        ctMethod.setModifiers(Modifier.PUBLIC);
        ctMethod.setBody("""
                {
                    System.out.println("执行方法中......");
                }
                """);
        cc.addMethod(ctMethod);

        cc.writeFile("./");
        Object o = cc.toClass().getDeclaredConstructor().newInstance();

        // 调用 personFly 方法
        Method personFlyMethod = o.getClass().getMethod("run");
        personFlyMethod.invoke(o);
        // 调用 joinFriend 方法
        Method execute = o.getClass().getMethod("run1");
        execute.invoke(o);
    }

    private static void aa() throws CannotCompileException, IOException {
        ClassPool classPool = ClassPool.getDefault();
        classPool.importPackage("org.luvx.coding.usage.javassist");
        CtClass cc = null;
        try {
            cc = classPool.get(classname);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        CtMethod[] methods = cc.getDeclaredMethods();

        for (int i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];
            String methodName = method.getName();
            String oldMethodName = methodName + "$$old";
            if (!"main".equalsIgnoreCase(methodName)) {
                CtMethod newMethod = CtNewMethod.copy(method, cc, null);
                method.setName(oldMethodName);

                String s = """
                        {
                            long s = System.currentTimeMillis();
                            %s($$);
                            System.out.println("方法(%s)执行花费:" + (System.currentTimeMillis()-s) + " ms.");
                        }
                        """;
                s = String.format(s, oldMethodName, methodName);

                newMethod.setBody(s);
                newMethod.setName(methodName);
                cc.addMethod(newMethod);
            }
        }
        cc.writeFile("/tmp/hello");
    }
}
