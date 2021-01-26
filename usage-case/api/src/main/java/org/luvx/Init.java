package org.luvx;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Init {
    static class Foo {
        int i = 1;

        Foo() {
            // 代码块修改为2
            log.info("1:{}", i);
            // 调用子类的方法, 子类未初始化, 因此返回0
            int x = getValue();
            log.info("2:{}", x);
        }

        {
            i = 2;
        }

        protected int getValue() {
            log.info("info1:{}", getClass().getSimpleName());
            return i;
        }
    }

    static class Bar extends Foo {
        int j = 1;

        Bar() {
            j = 4;
        }

        {
            j = 3;
        }

        @Override
        public int getValue() {
            log.info("info2:{}", getClass().getSimpleName());
            return j;
        }
    }

    public static void main(String... args) {
        Bar bar = new Bar();
        // 执行结果: 2 0 4
        log.info("3:{}", bar.getValue());
    }
}
