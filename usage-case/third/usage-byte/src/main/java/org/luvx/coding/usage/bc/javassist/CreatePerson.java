package org.luvx.coding.usage.bc.javassist;

import javassist.*;

public class CreatePerson {
    public static void createPerson() throws Exception {
        final String className = "org.luvx.coding.usage.javassist.Person";

        ClassPool pool = ClassPool.getDefault();

        // 1. 创建一个空类
        CtClass clazz = pool.makeClass(className);

        // 2. 新增一个字段 private String name;
        // 字段名为name
        CtField param = new CtField(pool.get("java.lang.String"), "name", clazz);
        // 访问级别是 private
        param.setModifiers(Modifier.PRIVATE);
        // 初始值是 "xiaoming"
        clazz.addField(param, CtField.Initializer.constant("foobar"));

        // 3. 生成 getter、setter 方法
        clazz.addMethod(CtNewMethod.setter("setName", param));
        clazz.addMethod(CtNewMethod.getter("getName", param));

        // 4. 添加无参的构造函数
        CtConstructor cons = new CtConstructor(new CtClass[]{}, clazz);
        cons.setBody("{name = \"barfoo\";}");
        clazz.addConstructor(cons);

        // 5. 添加有参的构造函数
        cons = new CtConstructor(new CtClass[]{pool.get("java.lang.String")}, clazz);
        // $0=this / $1,$2,$3... 代表方法参数
        cons.setBody("{$0.name = $1;}");
        clazz.addConstructor(cons);

        // 6. 创建一个名为printName方法，无参数，无返回值，输出name值
        CtMethod ctMethod = new CtMethod(CtClass.voidType, "printName", new CtClass[]{}, clazz);
        ctMethod.setModifiers(Modifier.PUBLIC);
        ctMethod.setBody("{System.out.println(name);}");
        clazz.addMethod(ctMethod);

        // 这里会将这个创建的类对象编译为.class文件
        clazz.writeFile("/tmp/hello");
    }

    public static void main(String[] args) {
        try {
            createPerson();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
