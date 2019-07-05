package org.luvx.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.luvx.util.RestTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName: org.luvx.controller
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/11 14:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {
    @Autowired
    RestTemplate restTemplate;

    String url;

    @Before
    public void init() {
        url = "http://127.0.0.1:8090/test/get";
    }

    @Test
    public void method() {
        ResponseEntity<String> entity = restTemplate.getForEntity(url, String.class);
        RestTemplateUtils.printResponse(entity);

        url = "http://127.0.0.1:8090/test/post/19";
        entity = restTemplate.postForEntity(url, null, String.class);
        RestTemplateUtils.printResponse(entity);
    }

    @Test
    public void method1() {
        ResponseEntity<String> entity = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        RestTemplateUtils.printResponse(entity);

        url = "http://127.0.0.1:8090/test/post/19";
        entity = restTemplate.exchange(url, HttpMethod.POST, null, String.class);
        RestTemplateUtils.printResponse(entity);
    }

    @Test
    public void method2() {
    }

    @Test
    public void method3() {

    }
}