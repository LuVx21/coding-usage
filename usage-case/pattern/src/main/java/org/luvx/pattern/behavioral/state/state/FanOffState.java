package org.luvx.pattern.behavioral.state.state;

import lombok.extern.slf4j.Slf4j;
import org.luvx.pattern.behavioral.state.FanContext;

@Slf4j
public class FanOffState extends FanState {
    @Override
    public void turnOff() {
        log.info("{} 关闭风扇", getClass().getSimpleName());
    }

    @Override
    public void shiftHigh() {
        fanContext.setFanState(FanContext.HIGH);
        fanContext.getFanState().shiftHigh();
    }

    @Override
    public void shiftLow() {
        fanContext.setFanState(FanContext.LOW);
        fanContext.getFanState().shiftLow();
    }
}
