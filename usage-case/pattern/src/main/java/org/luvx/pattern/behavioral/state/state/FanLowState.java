package org.luvx.pattern.behavioral.state.state;

import lombok.extern.slf4j.Slf4j;
import org.luvx.pattern.behavioral.state.FanContext;

@Slf4j
public class FanLowState extends FanState {
    @Override
    public void turnOff() {
        fanContext.setFanState(FanContext.OFF);
        fanContext.getFanState().turnOff();
    }

    @Override
    public void shiftHigh() {
        fanContext.setFanState(FanContext.HIGH);
        fanContext.getFanState().shiftHigh();
    }

    @Override
    public void shiftLow() {
        log.info("{} 调到低挡", getClass().getSimpleName());
    }
}
