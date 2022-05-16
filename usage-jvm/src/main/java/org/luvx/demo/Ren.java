package org.luvx.demo;

/**
 * @ClassName: org.luvx.demo
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/1 17:55
 */
public class Ren {

    // public static void main(String[] args) {
    //     Ren ren = new Ren();
    //     ren.method(1, 2);
    // }

    /// -----------------继承体系
    private static class Parent {
        private int i = 10;

        protected static void method() {
            System.out.println("parent static");
        }

        protected void method1() {
            System.out.println("parent not static");
        }
    }

    private static class Sub extends Parent {
        private int i = 100;

        protected static void method() {
            System.out.println("sub static");
        }

        @Override
        protected void method1() {
            System.out.println("sub not static");
        }
    }
    /// 成员属性/静态成员方法 编译和运行, 都参考左边,
    /// 只有非静态成员方法满足 编译看左边, 运行看右边
    public static void main(String[] args) {
        Parent p = new Sub();
        Sub s = new Sub();

        p.method();
        s.method();

        p.method1();
        s.method1();

        // 110 = 10 + 100
        System.out.println((p.i + s.i));
    }
    /// -----------------继承体系


    private boolean method(int a, Object obj) {
        System.out.println(3);
        return false;
    }


    private boolean method(int a, String str) {
        System.out.println(1);
        return false;
    }

    private int method(int a, Integer str) {
        System.out.println(2);
        return 0;
    }

    private boolean method(Integer a, String str) {
        System.out.println(4);
        return false;
    }
}
