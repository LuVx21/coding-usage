package org.luvx.pattern.behavioral.template_method;

/**
 * @Author: Ren, Xie
 * @Date: 2019/8/21 17:29
 */
public class LoginByPhone implements Login {
    @Override
    public void doLogin() {
        System.out.println("使用手机验证码登录");
    }
}
