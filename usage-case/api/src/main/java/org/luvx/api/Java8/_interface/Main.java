package org.luvx.api.Java8._interface;

import org.luvx.coding.common.more.MorePrints;

public class Main {
    public static void main(String[] args) {
        A1 a1 = new A1();
        A2 a2 = new A2();
        MorePrints.println(a1.doIt(), a2.doIt());
    }

    private static class A1 implements Interface1, Interface2 {
        /**
         * 两个接口有同名方法, 必须实现方法
         */
        @Override
        public String doIt() {
            return "A1";
        }
    }

    private static class A2 extends I1Impl1 implements Interface2 {
    }
}
