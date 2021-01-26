package org.luvx.pattern.behavioral.memento;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Manager {
    /**
     * 仅仅保存上一次的版本
     * 多次版本的场景可以使用集合或者数据库
     */
    private Memento memento;
}
