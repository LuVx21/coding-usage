package org.luvx.pattern.structural.decorator;


/**
 * 被装饰对象接口
 * 可以被定义为抽象类
 * <p>
 * 例:
 * 打印日志前后各执行一些功能
 */
public interface Printer {
    void printLog();
}
