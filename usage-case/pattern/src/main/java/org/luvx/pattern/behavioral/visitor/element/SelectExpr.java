package org.luvx.pattern.behavioral.visitor.element;

import org.luvx.pattern.behavioral.visitor.visitor.Visitor;

/**
 * @Author: Ren, Xie
 * @Date: 2019/7/9 9:29
 */
public class SelectExpr implements Element {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
