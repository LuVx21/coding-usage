package org.luvx.api._17;

/**
 * final: 不能进一步扩展
 * sealed: 限制扩展
 * non-sealed: 不限制扩展
 */
public sealed class UserServiceImpl implements UserService {
    @Override
    public void doSomething() {
    }

    final class A extends UserServiceImpl {
    }
}
