package org.luvx.util;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName: org.luvx.utils
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/21 11:55
 */
public class RestTemplateUtils {

    private static String       cookie = "";
    private static String       token  = "";
    private static RestTemplate restTemplate;

    @Autowired
    RestTemplateUtils(RestTemplate restTemplate) {
        RestTemplateUtils.restTemplate = restTemplate;
    }

    /**
     * 获取cookie信息
     * @return
     */
    public static CookieEntity getCookieEntity() {
        CookieEntity entity = new CookieEntity();
        entity.setCookie(cookie);
        entity.setToken(token);
        return entity;
    }

    public static List<String> setCookie(CookieEntity cookieEntity) {
        String cookieStr = cookieEntity.getCookie();
        String[] cookieArray = cookieStr.split("; ");
        final List<String> cookies = Arrays.asList(cookieArray);
        return cookies;
    }

    /**
     * 设置请求实体
     *
     * @param entity
     * @param referer 跳转来源: 为null不配置
     * @return
     */
    public static HttpEntity setHttpEntity(CookieEntity entity, String referer) {
        Objects.requireNonNull(entity, "cookie entity is null!");

        List<String> cookies = RestTemplateUtils.setCookie(entity);
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.COOKIE, cookies);

        if (StringUtils.isNotEmpty(referer)) {
            headers.put(HttpHeaders.REFERER, Arrays.asList(referer));
        }

        HttpEntity request = new HttpEntity(null, headers);
        return request;
    }

    /**
     * 发请求
     *
     * @param url
     * @param request
     * @return
     */
    public static String request(String url, HttpEntity request) {
        String json = null;
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
            HttpStatus status = response.getStatusCode();
            if (status.is2xxSuccessful()) {
                json = response.getBody();
            }
        } catch (Exception e) {
            throw new RuntimeException("请求失败:" + e.getMessage());
        }

        return json;
    }

    public static void printResponse(ResponseEntity<?> response) {
        HttpStatus status = response.getStatusCode();
        if (!status.is2xxSuccessful()) {
            System.out.println("非200");
        }
        System.out.println("-------------------------");
        System.out.println(response.getStatusCodeValue());
        System.out.println(response.getHeaders());
        System.out.println(response.getBody());
        System.out.println("-------------------------");
    }

    @Data
    private static class CookieEntity {
        private Long   id;
        private String cookie;
        private String token;
        private Date   insertTime;
    }
}
