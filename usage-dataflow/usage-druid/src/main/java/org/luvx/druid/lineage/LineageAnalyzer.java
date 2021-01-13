package org.luvx.druid.lineage;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.util.StringUtils;
import org.luvx.druid.lineage.pojo.LineageField;
import org.luvx.druid.lineage.pojo.TreeNode;
import org.luvx.druid.lineage.utils.EmptyUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ren, Xie
 */
public class LineageAnalyzer {
    /**
     * 解析sql
     *
     * @param sql
     * @param root
     */
    public static void mysqlFieldLineageAnalyzer(String sql, TreeNode<LineageField> root) {
        if (EmptyUtils.isEmpty(sql)) {
            return;
        }
        AtomicReference<Boolean> isContinue = new AtomicReference<>(false);
        List<SQLStatement> stmtList;
        try {
            stmtList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        } catch (Exception e) {
            throw new RuntimeException("解析异常, 请确认sql内容");
        }
        SQLStatement statement = stmtList.get(0);
        if (!(statement instanceof SQLSelectStatement)) {
            throw new RuntimeException("暂只支持select语句");
        }
        SQLSelectStatement selectStatement = (SQLSelectStatement) statement;
        SQLSelectQuery selectQuery = selectStatement.getSelect().getQuery();
        // 非union的查询语句
        if (selectQuery instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock selectQueryBlock = (SQLSelectQueryBlock) selectQuery;
            List<SQLSelectItem> selectItems = selectQueryBlock.getSelectList();
            selectItems.forEach(selectField -> {
                String alias = selectField.getAlias();
                String field = StringUtils.isEmpty(alias) ? selectField.toString() : alias;
                if (field.contains(".")) {
                    field = field.substring(field.indexOf(".") + 1);
                }
                field = field.replace("`", "");

                SQLExpr expr = selectField.getExpr();
                LineageField myColumn = new LineageField();
                myColumn.setField(field);
                myColumn.setExpression(expr.toString());

                TreeNode<LineageField> itemNode = new TreeNode<>(myColumn);
                handlerExpr(expr, itemNode);
                if (root.getLevel() == 0 || Objects.equals(field, root.getData().getField())) {
                    root.addChild(itemNode);
                    isContinue.set(true);
                }
            });
            if (isContinue.get()) {
                SQLTableSource table = selectQueryBlock.getFrom();
                if (table instanceof SQLExprTableSource) {
                    handlerSqlExprTableSource(root, (SQLExprTableSource) table);
                } else if (table instanceof SQLJoinTableSource) {
                    handlerSqlJoinTableSource(root, (SQLJoinTableSource) table);
                } else if (table instanceof SQLSubqueryTableSource) {
                    handlerSqlSubqueryTableSource(root, table);
                } else if (table instanceof SQLUnionQueryTableSource) {
                    handlerSqlUnionQueryTableSource(root, (SQLUnionQueryTableSource) table);
                } else if (table instanceof SQLLateralViewTableSource) {
                    // TODO
                } else if (table instanceof SQLValuesTableSource) {
                    // TODO
                }
            }
        } else if (selectQuery instanceof SQLUnionQuery) {
            SQLUnionQuery unionQuery = (SQLUnionQuery) selectQuery;
            mysqlFieldLineageAnalyzer(unionQuery.getLeft().toString(), root);
            mysqlFieldLineageAnalyzer(unionQuery.getRight().toString(), root);
        }
    }

    /**
     * 处理UNION子句
     *
     * @param node
     * @param table
     */
    private static void handlerSqlUnionQueryTableSource(TreeNode<LineageField> node, SQLUnionQueryTableSource table) {
        node.getAllLeafs().stream().filter(e -> !e.getData().getIsEnd()).forEach(e -> {
            mysqlFieldLineageAnalyzer(table.getUnion().toString(), e);
        });
    }

    /**
     * 处理sub子句
     *
     * @param node
     * @param table
     */
    private static void handlerSqlSubqueryTableSource(TreeNode<LineageField> node, SQLTableSource table) {
        node.getAllLeafs().stream().filter(e -> !e.getData().getIsEnd()).forEach(e -> {
            mysqlFieldLineageAnalyzer(table.toString(), e);
        });
    }


    /**
     * 处理JOIN
     *
     * @param node
     * @param table
     */
    private static void handlerSqlJoinTableSource(TreeNode<LineageField> node, SQLJoinTableSource table) {
        // 处理---------------------
        // 子查询作为表
        node.getAllLeafs().stream().filter(e -> !e.getData().getIsEnd()).forEach(e -> {
            if (table.getLeft() instanceof SQLJoinTableSource) {
                handlerSqlJoinTableSource(node, (SQLJoinTableSource) table.getLeft());
            } else if (table.getLeft() instanceof SQLExprTableSource) {
                handlerSqlExprTableSource(node, (SQLExprTableSource) table.getLeft());
            } else if (table.getLeft() instanceof SQLSubqueryTableSource) {
                // 处理---------------------
                handlerSqlSubqueryTableSource(node, table.getLeft());
            } else if (table.getLeft() instanceof SQLUnionQueryTableSource) {
                // 处理---------------------
                handlerSqlUnionQueryTableSource(node, (SQLUnionQueryTableSource) table.getLeft());
            }
        });

        node.getAllLeafs().stream().filter(e -> !e.getData().getIsEnd()).forEach(e -> {
            if (table.getRight() instanceof SQLJoinTableSource) {
                handlerSqlJoinTableSource(node, (SQLJoinTableSource) table.getRight());
            } else if (table.getRight() instanceof SQLExprTableSource) {
                handlerSqlExprTableSource(node, (SQLExprTableSource) table.getRight());
            } else if (table.getRight() instanceof SQLSubqueryTableSource) {
                // 处理---------------------
                handlerSqlSubqueryTableSource(node, table.getRight());
            } else if (table.getRight() instanceof SQLUnionQueryTableSource) {
                // 处理---------------------
                handlerSqlUnionQueryTableSource(node, (SQLUnionQueryTableSource) table.getRight());
            }
        });
    }

    /**
     * 处理最终表
     *
     * @param node
     * @param table
     */
    private static void handlerSqlExprTableSource(TreeNode<LineageField> node, SQLExprTableSource table) {
        SQLExprTableSource tableSource = table;
        String db = tableSource.getExpr() instanceof SQLPropertyExpr ? ((SQLPropertyExpr) tableSource.getExpr()).getOwner().toString().replace("`", "") : "";
        String tableName = tableSource.getExpr() instanceof SQLPropertyExpr ? ((SQLPropertyExpr) tableSource.getExpr()).getName().replace("`", "") : "";
        String alias = EmptyUtils.isNotEmpty(tableSource.getAlias()) ? tableSource.getAlias().replace("`", "") : "";

        node.getChildren().forEach(e -> {
            e.getChildren().forEach(f -> {
                if (EmptyUtils.isNotEmpty(db)) {
                    f.getData().setSourceDbName(db);
                }
                String sourceTableName = f.getData().getSourceTableName();
                if (sourceTableName == null
                        || Objects.equals(sourceTableName, tableName)
                        || Objects.equals(sourceTableName, alias)) {
                    f.getData().setSourceTableName(tableSource.toString());
                    f.getData().setIsEnd(true);
                    f.getData().setExpression(e.getData().getExpression());
                }
            });
        });
    }

    /**
     * 处理表达式
     *
     * @param sqlExpr
     * @param itemNode
     */
    private static void handlerExpr(SQLExpr sqlExpr, TreeNode<LineageField> itemNode) {
        //方法
        if (sqlExpr instanceof SQLMethodInvokeExpr) {
            visitSqlMethodInvoke((SQLMethodInvokeExpr) sqlExpr, itemNode);
        }
        //聚合
        else if (sqlExpr instanceof SQLAggregateExpr) {
            visitSqlAggregateExpr((SQLAggregateExpr) sqlExpr, itemNode);
        }
        //case
        else if (sqlExpr instanceof SQLCaseExpr) {
            visitSqlCaseExpr((SQLCaseExpr) sqlExpr, itemNode);
        }
        //比较
        else if (sqlExpr instanceof SQLBinaryOpExpr) {
            visitSqlBinaryOpExpr((SQLBinaryOpExpr) sqlExpr, itemNode);
        }
        //表达式
        else if (sqlExpr instanceof SQLPropertyExpr) {
            visitSqlPropertyExpr((SQLPropertyExpr) sqlExpr, itemNode);
        }
        //列
        else if (sqlExpr instanceof SQLIdentifierExpr) {
            visitSqlIdentifierExpr((SQLIdentifierExpr) sqlExpr, itemNode);
        }
        //赋值表达式
        else if (sqlExpr instanceof SQLIntegerExpr) {
            visitSqlIntegerExpr((SQLIntegerExpr) sqlExpr, itemNode);
        }
        //数字
        else if (sqlExpr instanceof SQLNumberExpr) {
            visitSqlNumberExpr((SQLNumberExpr) sqlExpr, itemNode);
        }
        //字符
        else if (sqlExpr instanceof SQLCharExpr) {
            visitSqlCharExpr((SQLCharExpr) sqlExpr, itemNode);
        }
    }

    /**
     * 方法
     *
     * @param expr
     * @param node
     */
    public static void visitSqlMethodInvoke(SQLMethodInvokeExpr expr, TreeNode<LineageField> node) {
        List<SQLExpr> parameters = expr.getParameters();
        if (parameters.size() == 0) {
            //计算表达式，没有更多列，结束循环
            if (Objects.equals(node.getData().getExpression(), expr.toString())) {
                node.getData().setIsEnd(true);
            }
        } else {
            parameters.forEach(expr1 -> {
                handlerExpr(expr1, node);
            });
        }
    }

    /**
     * 聚合
     *
     * @param expr
     * @param node
     */
    public static void visitSqlAggregateExpr(SQLAggregateExpr expr, TreeNode<LineageField> node) {
        expr.getArguments().forEach(expr1 -> {
            handlerExpr(expr1, node);
        });
    }

    /**
     * 选择
     *
     * @param expr
     * @param node
     */
    public static void visitSqlCaseExpr(SQLCaseExpr expr, TreeNode<LineageField> node) {
        expr.getItems().forEach(expr1 -> {
            handlerExpr(expr1.getConditionExpr(), node);
        });
    }

    /**
     * 判断
     *
     * @param expr
     * @param node
     */
    public static void visitSqlBinaryOpExpr(SQLBinaryOpExpr expr, TreeNode<LineageField> node) {
        handlerExpr(expr.getLeft(), node);
        handlerExpr(expr.getRight(), node);
    }

    /**
     * 表达式列
     *
     * @param expr
     * @param node
     */
    public static void visitSqlPropertyExpr(SQLPropertyExpr expr, TreeNode<LineageField> node) {
        LineageField project = new LineageField();
        String columnName = expr.getName().replace("`", "");
        project.setField(columnName);

        project.setSourceTableName(expr.getOwner().toString());
        TreeNode<LineageField> search = node.findChildNode(project);

        if (EmptyUtils.isEmpty(search)) {
            node.addChild(project);
        }
    }

    /**
     * 列
     *
     * @param expr
     * @param node
     */
    public static void visitSqlIdentifierExpr(SQLIdentifierExpr expr, TreeNode<LineageField> node) {
        LineageField project = new LineageField();
        project.setField(expr.getName());

        TreeNode<LineageField> search = node.findChildNode(project);
        if (EmptyUtils.isEmpty(search)) {
            node.addChild(project);
        }
    }

    /**
     * 整型赋值
     *
     * @param expr
     * @param node
     */
    public static void visitSqlIntegerExpr(SQLIntegerExpr expr, TreeNode<LineageField> node) {
        LineageField project = new LineageField();
        project.setField(expr.getNumber().toString());
        //常量不设置表信息
        project.setSourceTableName("");
        project.setIsEnd(true);
        TreeNode<LineageField> search = node.findChildNode(project);
        if (EmptyUtils.isEmpty(search)) {
            node.addChild(project);
        }
    }

    /**
     * 数字
     *
     * @param expr
     * @param node
     */
    public static void visitSqlNumberExpr(SQLNumberExpr expr, TreeNode<LineageField> node) {
        LineageField project = new LineageField();
        project.setField(expr.getNumber().toString());
        //常量不设置表信息
        project.setSourceTableName("");
        project.setIsEnd(true);
        TreeNode<LineageField> search = node.findChildNode(project);
        if (EmptyUtils.isEmpty(search)) {
            node.addChild(project);
        }
    }

    /**
     * 字符
     *
     * @param expr
     * @param node
     */
    public static void visitSqlCharExpr(SQLCharExpr expr, TreeNode<LineageField> node) {
        LineageField project = new LineageField();
        project.setField(expr.toString());
        //常量不设置表信息
        project.setSourceTableName("");
        project.setIsEnd(true);
        TreeNode<LineageField> search = node.findChildNode(project);
        if (EmptyUtils.isEmpty(search)) {
            node.addChild(project);
        }
    }
}
