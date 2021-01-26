package org.luvx.pattern.behavioral.mediator;

import lombok.extern.slf4j.Slf4j;
import org.luvx.pattern.behavioral.mediator.base.Mediator;

@Slf4j
public class Mediator_A extends Mediator {
    @Override
    public void rental() {
        log.info("{}中介负责出租", getClass().getSimpleName());
    }

    @Override
    public void lessee() {
        log.info("{}中介负责承租", getClass().getSimpleName());
    }
}
