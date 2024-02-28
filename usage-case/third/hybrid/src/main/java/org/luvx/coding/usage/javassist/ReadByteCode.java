package org.luvx.coding.usage.javassist;

import javassist.*;

import java.io.IOException;

public class ReadByteCode {

    public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException {
        ClassPool classPool = ClassPool.getDefault();
        classPool.importPackage("org.luvx.coding.usage.javassist");
        CtClass ctClass = null;
        try {
            ctClass = classPool.get("org.luvx.coding.usage.javassist.Bool");
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        CtMethod[] methods = ctClass.getDeclaredMethods();

        for (int i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];
            String methodName = method.getName();
            String oldMethodName = STR."\{methodName}$$old";
            if (!"main".equalsIgnoreCase(methodName)) {
                CtMethod newMethod = CtNewMethod.copy(method, ctClass, null);
                method.setName(oldMethodName);

                String s = STR."""
                        {
                            long s = System.currentTimeMillis();
                            \{oldMethodName}($$);
                            System.out.println("方法(\{methodName})执行花费:" + (System.currentTimeMillis()-s) + " ms.");
                        }
                        """;

                newMethod.setBody(s);
                newMethod.setName(methodName);
                ctClass.addMethod(newMethod);
            }
        }
        ctClass.writeFile("/tmp/hello");
    }
}
