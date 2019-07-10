package org.luvx.er.interceptor;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: org.luvx.er.interceptor
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/7/10 17:11
 */
public class Interceptor2 extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        System.out.println(getClass().getSimpleName() + ":拦截到请求:" + request);
    }
}
