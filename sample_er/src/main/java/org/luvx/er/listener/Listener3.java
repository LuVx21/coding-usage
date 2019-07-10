package org.luvx.er.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @ClassName: org.luvx.listener
 * @Description: 有序
 * @Author: Ren, Xie
 * @Date: 2019/7/10 11:15
 */
@Component
public class Listener3 implements SmartApplicationListener {
    /**
     * 判断事件类型用
     *
     * @param eventType
     * @return
     */
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return true;
    }

    /**
     * 判断事件源用
     *
     * @param sourceType
     * @return
     */
    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

    /**
     * supportsEventType 和 supportsSourceType 方法同时返回true时才会执行此方法
     * 因此用于判断是否是需要监听的
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println(getClass().getSimpleName() + ":监听到事件:" + event.hashCode());
        // System.out.println(event.getClass().getName());
        // ((Event) event).click();
    }

    /**
     * 保证顺序
     *
     * @return 值越小优先级越高
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
