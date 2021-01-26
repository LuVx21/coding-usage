package org.luvx.pattern.behavioral.observer;

/**
 * @Author: Ren, Xie
 * @Date: 2019/7/9 10:44
 */
public class Driver extends Observer {

    @Override
    public void update() {
        System.out.println("观察到红路灯变化:" + this);
    }
}
