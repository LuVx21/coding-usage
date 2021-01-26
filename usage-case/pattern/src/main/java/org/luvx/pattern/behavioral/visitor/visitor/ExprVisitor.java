package org.luvx.pattern.behavioral.visitor.visitor;

import lombok.extern.slf4j.Slf4j;
import org.luvx.pattern.behavioral.visitor.element.FromExpr;
import org.luvx.pattern.behavioral.visitor.element.SelectExpr;
import org.luvx.pattern.behavioral.visitor.element.WhereExpr;

/**
 * @Author: Ren, Xie
 * @Date: 2019/7/9 9:29
 */
@Slf4j
public class ExprVisitor implements Visitor {
    @Override
    public void visit(SelectExpr selectExpr) {
        log.info("visit {}", selectExpr.getClass().getSimpleName());
    }

    @Override
    public void visit(FromExpr fromExpr) {
        log.info("visit {}", fromExpr.getClass().getSimpleName());
    }

    @Override
    public void visit(WhereExpr whereExpr) {
        log.info("visit {}", whereExpr.getClass().getSimpleName());
    }
}

