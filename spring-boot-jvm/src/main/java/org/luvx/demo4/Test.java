package org.luvx.demo4;

/**
 * @ClassName: org.luvx.demo4
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/1 20:17
 */
public class Test {
    public static void main(String[] args) {
        // method1();
        // method2();
        method3();
    }

    public static void method1() {
        MyNode mn = new MyNode(5);
        // A raw type - compiler throws an unchecked warning
        Node n = mn;
        n.setData("Hello");
        // Causes a ClassCastException to be thrown.
        Integer x = mn.data;
    }

    public static void method2() {
        MyNode mn = new MyNode(5);
        // A raw type - compiler throws an unchecked warning
        Node n = (MyNode) mn;
        n.setData("Hello");
        // Causes a ClassCastException to be thrown.
        // Integer x = (String) mn.data;
    }

    public static void method3() {
        Node mn = new MyNode(5);
        /// 调用实际方法
        mn.setData(new Integer(5));
        /// 调用桥接方法
        mn.setData(new Object());
    }

    public static void method4() {
        Node<Integer> mn = new MyNode(5);
        /// 调用实际方法
        mn.setData(new Integer(5));
        /// 调用桥接方法
        // mn.setData(new Object());
    }
}
