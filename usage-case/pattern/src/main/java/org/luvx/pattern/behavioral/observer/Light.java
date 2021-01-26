package org.luvx.pattern.behavioral.observer;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ren, Xie
 * @Date: 2019/7/9 10:42
 */
@Getter
@Setter
public class Light extends Subject {

    private int status;

    @Override
    public void notifyObserver() {
        for (Observer ob : observers) {
            ob.update();
        }
    }
}
