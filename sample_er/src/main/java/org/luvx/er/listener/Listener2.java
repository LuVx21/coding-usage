package org.luvx.er.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName: org.luvx.listener
 * @Description: 注解方式
 * @Author: Ren, Xie
 * @Date: 2019/7/10 11:15
 */
@Component
public class Listener2 {
    @EventListener
    public void onApplicationEvent(Event event) {
        System.out.println(getClass().getSimpleName() + ":监听到事件:" + event.hashCode());
        event.click();
    }
}
