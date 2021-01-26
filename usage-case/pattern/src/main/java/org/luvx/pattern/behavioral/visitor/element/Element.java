package org.luvx.pattern.behavioral.visitor.element;

import org.luvx.pattern.behavioral.visitor.visitor.Visitor;

/**
 * @Author: Ren, Xie
 * @Date: 2019/7/9 9:22
 */
public interface Element {
    void accept(Visitor visitor);
}
