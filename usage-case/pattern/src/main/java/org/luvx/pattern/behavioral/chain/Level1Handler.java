package org.luvx.pattern.behavioral.chain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Level1Handler extends Handler {
    @Override
    public void handle(int amount) {
        if (amount < 500) {
            log.info("{}批准了经费申请, 金额:{}", getClass().getSimpleName(), amount);
        } else {
            next(amount);
        }
    }
}
