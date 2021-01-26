package org.luvx.pattern.behavioral.visitor.visitor;

import org.luvx.pattern.behavioral.visitor.element.FromExpr;
import org.luvx.pattern.behavioral.visitor.element.SelectExpr;
import org.luvx.pattern.behavioral.visitor.element.WhereExpr;

/**
 * @Author: Ren, Xie
 * @Date: 2019/7/9 9:22
 */
public interface Visitor {
    void visit(SelectExpr selectExpr);

    void visit(FromExpr fromExpr);

    void visit(WhereExpr whereExpr);
}
