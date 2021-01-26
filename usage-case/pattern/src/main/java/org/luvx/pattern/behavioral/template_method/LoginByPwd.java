package org.luvx.pattern.behavioral.template_method;

/**
 * @Author: Ren, Xie
 * @Date: 2019/8/21 17:30
 */
public class LoginByPwd implements Login {
    @Override
    public void doLogin() {
        System.out.println("使用账户密码登录");
    }
}
