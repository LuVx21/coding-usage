package org.luvx.pattern.behavioral.memento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 备忘类, 记录内容的修改
 *
 * @author: Ren, Xie
 */
@Getter
@Setter
@AllArgsConstructor
public class Memento {
    private String src;
}
