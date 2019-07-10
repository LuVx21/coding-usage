package org.luvx.config;

import org.luvx.er.filter.Filter1;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: org.luvx.config
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/7/10 16:53
 */
@Configuration
@ServletComponentScan("org.luvx.er.filter")
public class FilterConfig {

    @Bean
    public FilterRegistrationBean firstFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new Filter1());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("filter1");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
