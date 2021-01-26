package org.luvx.pattern.behavioral.command;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Soldier {
    public void fire() {
        log.info("{}执行命令", getClass().getSimpleName());
    }
}
