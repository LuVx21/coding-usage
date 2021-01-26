package org.luvx.pattern.behavioral.visitor.element;

import org.luvx.pattern.behavioral.visitor.visitor.Visitor;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: Ren, Xie
 * @Date: 2019/7/9 9:29
 */
public class SqlExpr implements Element {
    List<Element> list = Arrays.asList(new SelectExpr(), new FromExpr(), new WhereExpr());

    @Override
    public void accept(Visitor visitor) {
        for (Element expr : list) {
            expr.accept(visitor);
        }
    }
}
