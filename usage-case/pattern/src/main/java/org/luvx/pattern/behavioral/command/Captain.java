package org.luvx.pattern.behavioral.command;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Captain {
    @Setter
    private Command command;

    public Captain(Command command) {
        this.command = command;
    }

    public void invoke() {
        log.info("{} 下达命令", getClass().getSimpleName());
        command.execute();
    }
}
