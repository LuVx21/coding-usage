package org.luvx.pattern.behavioral.memento;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class Client {
    @Test
    public void test1() {
        Code code = new Code();
        code.setSrc("select * from table");
        log.info("版本1:{}", code.getSrc());
        Manager manager = new Manager();
        manager.setMemento(code.createMemento());

        code.setSrc("select * from table1");
        log.info("版本2:{}", code.getSrc());

        code.restoreMemento(manager.getMemento());
        log.info("版本3:{}", code.getSrc());
    }
}