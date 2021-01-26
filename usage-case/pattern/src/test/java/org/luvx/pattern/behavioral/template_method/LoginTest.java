package org.luvx.pattern.behavioral.template_method;

import org.junit.Test;

/**
 * @Author: Ren, Xie
 * @Date: 2019/8/21 17:32
 */
public class LoginTest {
    @Test
    public void login() {
        Login login = new LoginByPwd();
        login.login();
        login = new LoginByPhone();
        login.login();
    }
}
