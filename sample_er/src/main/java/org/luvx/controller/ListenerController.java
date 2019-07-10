package org.luvx.controller;

import org.luvx.er.listener.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: org.luvx.controller
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/7/10 14:41
 */
@RestController
public class ListenerController {

    @Autowired
    ApplicationContext        applicationContext;
    @Autowired
    ApplicationEventPublisher publisher;

    @GetMapping(value = "/l")
    public String register() {
        applicationContext.publishEvent(new Event(this));
        publisher.publishEvent(new Event(this));
        return "成功";
    }
}
