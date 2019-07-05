package org.luvx.controller;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: org.luvx.controller
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/6/5 17:38
 */
@RestController
@RequestMapping(value = "/test")
public class UserController {

    @Data
    @Builder
    private static class User {
        private Long    userId;
        private String  userName;
        private String  password;
        private Integer age;
    }

    @RequestMapping(value = {"/get"}, method = RequestMethod.GET)
    public String get() {
        User user = User.builder().userId(100L).userName("foo").password("bar").age(18).build();
        try {
            Thread.sleep(25 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return JSON.toJSONString(user);
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.POST)
    public String post(@PathVariable int id) {
        User user = User.builder().userId(100L).userName("foo").password("bar").age(id).build();
        return JSON.toJSONString(user);
    }
}
