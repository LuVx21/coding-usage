package org.luvx.pattern.behavioral.state;

import lombok.Getter;
import org.luvx.pattern.behavioral.state.state.FanHighState;
import org.luvx.pattern.behavioral.state.state.FanLowState;
import org.luvx.pattern.behavioral.state.state.FanOffState;
import org.luvx.pattern.behavioral.state.state.FanState;

public class FanContext {
    public final static FanState OFF  = new FanOffState();
    public final static FanState LOW  = new FanLowState();
    public final static FanState HIGH = new FanHighState();

    @Getter
    private FanState fanState;

    public FanContext() {
        fanState = OFF;
        fanState.setFanContext(this);
    }

    public void setFanState(FanState fanState) {
        this.fanState = fanState;
        fanState.setFanContext(this);
    }

    public void turnOff() {
        fanState.turnOff();
    }

    public void shiftHigh() {
        fanState.shiftHigh();
    }

    public void shiftLow() {
        fanState.shiftLow();
    }
}
