package org.luvx.er.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @ClassName: org.luvx.er.filter
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/7/10 16:46
 */
public class Filter1 implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println(getClass().getSimpleName() + ":过滤到请求:" + request);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
