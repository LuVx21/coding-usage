package org.luvx.api._17;

import java.io.Serializable;

/**
 * sealed: 限制其他哪些类/接口可以扩展或实现它
 * permits: 指定可以继承或实现该类的类型
 */
public sealed interface UserService
        extends Serializable
        permits UserServiceImpl {
    void doSomething();
}
