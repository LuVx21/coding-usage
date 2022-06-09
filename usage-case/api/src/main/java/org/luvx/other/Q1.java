package org.luvx.other;

import org.luvx.common.more.MorePrints;

/**
 * 父子类属性名相同
 */
public class Q1 {
    private static class Parent {
        private int i = 10;
    }

    private static class Sub extends Parent {
        private int i = 100;
    }

    public static void main(String[] args) {
        Parent p = new Sub();
        Sub s = new Sub();
        // 10 100
        MorePrints.println(p.i, s.i);
    }
}

