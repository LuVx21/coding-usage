package org.luvx.er.listener;

import org.springframework.context.ApplicationEvent;

/**
 * @ClassName: org.luvx.listener
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/7/10 11:14
 */
public class Event extends ApplicationEvent {
    private String name;

    public Event(Object source) {
        super(source);
    }

    public void click() {
        // name = System.currentTimeMillis() + "";
        // System.out.println("点击事件" + name);
    }
}
