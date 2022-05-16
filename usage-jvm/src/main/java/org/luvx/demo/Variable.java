package org.luvx.demo;

/**
 * @ClassName: org.luvx.demo
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/1 15:26
 */
public class Variable {
    public static void main(String[] args) {
        Variable variable = new Variable();
        /// variable.method(1, 2);
        /// 调用第2个
        variable.invoke(null, 1);
        variable.invoke(null, 1, 2);
        /// 调用第一个方法, 可变长参数的实现就是数组, 这里定义一个Object数组,
        /// 这个数组就是指代args, 即指定第二个参数是个数组
        variable.invoke(null, new Object[]{1});
    }

    void invoke(Object obj, Object... args) {
    }

    void invoke(String s, Object obj, Object... args) {
    }
}
