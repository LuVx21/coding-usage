package org.luvx.pattern.behavioral.template_method;

interface Login {
    /**
     * 登录环境安全性检查
     */
    default void envCheck() {
        System.out.println("检查当前登录环境......当前登录环境无风险");
    }

    /**
     * 登录功能, 支持不同的登录方式
     * 账号密码
     * 手机验证码
     * 社交网络账号
     */
    void doLogin();

    /**
     * 登录
     */
    default void login() {
        envCheck();
        doLogin();
        pageSwitch();
    }

    /**
     * 登录后页面跳转
     */
    default void pageSwitch() {
        System.out.println("登录成功, 跳转至主页面, 请稍后\n");
    }
}
