package org.luvx.er.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName: org.luvx.listener
 * @Description: 实现接口方式
 * @Author: Ren, Xie
 * @Date: 2019/7/10 11:15
 */
@Component
public class Listener1 implements ApplicationListener<Event> {
    @Override
    public void onApplicationEvent(Event event) {
        System.out.println(getClass().getSimpleName() + ":监听到事件:" + event.hashCode());
        event.click();
    }
}
