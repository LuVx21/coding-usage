package org.luvx.config;

import org.luvx.er.interceptor.Interceptor1;
import org.luvx.er.interceptor.Interceptor2;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName: org.luvx.config
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/7/10 17:14
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor1())
                .addPathPatterns("/**")
                .order(1);

        registry.addInterceptor(new Interceptor2())
                .addPathPatterns("/**")
                .order(2);
    }
}
