package org.luvx.demo5;

/**
 * @ClassName: org.luvx.demo4
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/1 20:17
 */
public class Test {
    public static void main(String[] args) {
        method1();
        method2();
    }

    public static void method1() {
        MyNode mn = new MyNode(5);
        Node n = mn;
        n.setData("Hello");
        // Integer x = mn.data;
    }

    public static void method2() {
        MyNode mn = new MyNode(5);
        Node n = (MyNode) mn;
        n.setData("Hello");
        // Integer x = (String) mn.data;
    }
}
