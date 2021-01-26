package org.luvx.pattern.behavioral.state.state;

import lombok.Setter;
import org.luvx.pattern.behavioral.state.FanContext;

public abstract class FanState {
    @Setter
    protected FanContext fanContext;

    public abstract void turnOff();

    public abstract void shiftHigh();

    public abstract void shiftLow();
}
