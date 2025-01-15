package org.luvx.coding.usage.bc.javassist;

import javassist.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

class JavassistGeneratorTest {
    static String path = "/Users/renxie/OneDrive/Code/coding-usage/usage-case/third/usage-byte/src/test/java";

    @Test
    void m1() throws Exception {
        createClass();
    }

    public static void createClass() throws Exception {
        ClassPool classPool = ClassPool.getDefault();
        CtClass classUser = classPool.makeClass("org.luvx.coding.usage.bc.entity.User");

        String[] fields = {"id", "name", "password", "age"};
        Class[] types = {Long.class, String.class, String.class, Integer.class};

        addField(classPool, classUser, fields, types);

        addConstructor(classPool, classUser, fields, types);
        addToString(classPool, classUser, fields);

        addMethod(classUser);

        // 代表class文件的CtClass创建完成，现在将其转换成class对象
        Class clazz = classUser.toClass();

        classUser.writeFile(path);
    }

    /**
     * 简单创建方法方式
     *
     * @param cc
     * @throws Exception
     */
    public static void addMethod(CtClass cc) throws Exception {
        CtMethod method = CtNewMethod.make("public void code(){}", cc);
        method.insertBefore("System.out.println(\"I'm a Programmer,Just Coding.....\");");
        cc.addMethod(method);
    }

    /**
     * toString方法
     *
     * @param classPool
     * @param clazz
     * @throws Exception
     */
    public static void addToString(ClassPool classPool, CtClass clazz, String[] fields) throws Exception {
        CtClass returnType = classPool.get(String.class.getName());
        CtMethod toStringMethod = new CtMethod(returnType, "toString", null, clazz);
        toStringMethod.setModifiers(Modifier.PUBLIC);

        StringBuilder sb = new StringBuilder("{return ");
        String s = Arrays.stream(fields)
                .map(field -> "\"" + field + ":\"" + "+$0." + field)
                .collect(Collectors.joining(" + "));
        sb.append(s).append(";}");

        //$0表示的是this
        toStringMethod.setBody(sb.toString());
        clazz.addMethod(toStringMethod);
    }

    /**
     * 指定属性的有参构造函数
     *
     * @param classPool
     * @param clazz
     * @param fields
     * @throws Exception
     */
    public static void addConstructor(ClassPool classPool, CtClass clazz, String[] fields, Class[] types) throws Exception {
        CtClass[] parameters = new CtClass[types.length];
        for (int i = 0; i < types.length; i++) {
            parameters[i] = classPool.get(types[i].getName());
        }
        CtConstructor constructor = new CtConstructor(parameters, clazz);
        // 方法体 $1表示的第一个参数
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < fields.length; i++) {
            sb.append("this." + fields[i] + "=$" + (i + 1) + ";");
        }
        sb.append("}");
        constructor.setBody(sb.toString());
        clazz.addConstructor(constructor);
    }

    public static void addField(ClassPool classPool, CtClass clazz, String[] fields, Class[] types) throws Exception {
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i];
            Class type = types[i];
            CtClass fieldType = classPool.get(type.getName());
            CtField field = new CtField(fieldType, name, clazz);
            field.setModifiers(Modifier.PRIVATE);
            clazz.addField(field);
            /// classUser.addField(field, CtField.Initializer.constant("javasssit"));

            String temp = StringUtils.capitalize(name);
            clazz.addMethod(CtNewMethod.setter("set" + temp, field));
            clazz.addMethod(CtNewMethod.getter("get" + temp, field));
        }
    }
}
