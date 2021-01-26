package org.luvx.pattern.behavioral.state;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class Client {
    @Test
    public void a() {
        FanContext context = new FanContext();
        log.info("现在状态:{}", context.getFanState().getClass().getSimpleName());
        context.shiftHigh();
        log.info("现在状态:{}", context.getFanState().getClass().getSimpleName());
        context.shiftLow();
        log.info("现在状态:{}", context.getFanState().getClass().getSimpleName());
        context.turnOff();
        log.info("现在状态:{}", context.getFanState().getClass().getSimpleName());
    }
}