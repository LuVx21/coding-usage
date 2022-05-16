package org.luvx.demo;

public class MethodTest {

    public String method1(String str) {
        String mtdName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println("invoke " + mtdName + " return String");
        return "";
    }

    public int method2(String str) {
        String mtdName = Thread.currentThread().getStackTrace()[1].getMethodName();
        System.out.println("invoke " + mtdName + " return int");
        return 1;
    }

    public static void main(String[] args) {
        MethodTest javacTestOverload = new MethodTest();
        String str = javacTestOverload.method1("Test");
        int i = javacTestOverload.method2("Test");
    }
}
