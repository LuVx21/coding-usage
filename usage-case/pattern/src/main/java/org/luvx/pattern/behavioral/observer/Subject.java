package org.luvx.pattern.behavioral.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Ren, Xie
 * @Date: 2019/7/9 10:27
 */
public abstract class Subject {

    protected List<Observer> observers = new ArrayList();

    /**
     * 注册方法，用于向观察者集合中增加一个观察者
     *
     * @param observer
     */
    public void attach(Observer observer) {
        observers.add(observer);
    }

    /**
     * 注销方法，用于在观察者集合中删除一个观察者
     *
     * @param observer
     */
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    /**
     * 声明抽象通知方法
     */
    public abstract void notifyObserver();
}
