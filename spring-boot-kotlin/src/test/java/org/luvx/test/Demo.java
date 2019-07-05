package org.luvx.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.luvx.util.RestTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @ClassName: org.luvx.demo
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/5 17:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Demo {
    @Autowired
    RestTemplate restTemplate;

    // String url = "https://cap.cloud.alipay.com/tenant/sidebarCors.json?ctoken=w2_js9Gqn3zPUSlY&appId=3";
    String url = "https://jss.cloud.alipay.com/task/task.json?busiFlowId=&pageIndex=1&memory=false&silence=false&pageSize=30&method=queryTasksByPage&ctoken=w2_js9Gqn3zPUSlY";
    // String url = "http://211.159.175.179/";

    @Before
    public void init() {
        url = "https://dqs.cloud.alipay.com/scriptFileAjax.json?ctoken=w2_js9Gqn3zPUSlY&method=listScriptDir&fileKey=pubdir_0000071817_1235614379_57c10462-7a78-4a4d-a6a4-b6538d9cf129";
    }

    @Test
    public void method() {
        setCookie();
    }

    public void setCookie() {
        List<String> cookies = RestTemplateUtils.getCookie(null);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity request = new HttpEntity(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        RestTemplateUtils.printResponse(response);

        url = "https://dqs.cloud.alipay.com/scriptFileAjax.json?ctoken=w2_js9Gqn3zPUSlY&method=listScriptDir&fileKey=3fb0624a-96f1-42d7-9471-08309efa6632";
        response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        RestTemplateUtils.printResponse(response);
    }
}
