package org.luvx.pattern.behavioral.state.state;

import lombok.extern.slf4j.Slf4j;
import org.luvx.pattern.behavioral.state.FanContext;

@Slf4j
public class FanHighState extends FanState {
    @Override
    public void turnOff() {
        fanContext.setFanState(FanContext.OFF);
        fanContext.getFanState().turnOff();
    }

    @Override
    public void shiftHigh() {
        log.info("{} 调到高挡", getClass().getSimpleName());
    }

    @Override
    public void shiftLow() {
        fanContext.setFanState(FanContext.LOW);
        fanContext.getFanState().shiftLow();
    }
}
