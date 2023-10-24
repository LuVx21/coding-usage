package org.luvx.pattern.behavioral.visitor;

import org.junit.jupiter.api.Test;
import org.luvx.pattern.behavioral.visitor.element.SqlExpr;
import org.luvx.pattern.behavioral.visitor.visitor.ExprVisitor;

/**
 * @Author: Ren, Xie
 * @Date: 2019/7/9 9:52
 */
public class MainTest {
    @Test
    public void mainTest() {
        SqlExpr sqlExpr = new SqlExpr();
        sqlExpr.accept(new ExprVisitor());
    }
}
