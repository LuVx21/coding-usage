package org.luvx.pattern.behavioral.memento;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: Ren, Xie
 */
@Slf4j
@Getter
@Setter
public class Code {
    private String src;

    public Memento createMemento() {
        log.info("创建备份:{}", src);
        return new Memento(src);
    }

    public void restoreMemento(Memento m) {
        src = m.getSrc();
        log.info("恢复为上一版本:{}", src);
    }
}
