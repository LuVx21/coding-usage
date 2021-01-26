package org.luvx.pattern.behavioral.chain;

import lombok.Getter;
import lombok.Setter;

public abstract class Handler {
    @Getter
    @Setter
    private Handler nextHandler;

    /**
     * 处理逻辑
     *
     * @param amount
     */
    protected abstract void handle(int amount);

    protected void next(int amount) {
        Handler next = getNextHandler();
        if (next != null) {
            next.handle(amount);
        }
    }
}
