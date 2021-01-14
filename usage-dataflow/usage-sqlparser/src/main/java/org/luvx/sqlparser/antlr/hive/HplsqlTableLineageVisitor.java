package org.luvx.sqlparser.antlr.hive;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.RuleContext;
import org.luvx.sqlparser.antlr.hive.HplsqlBaseVisitor;
import org.luvx.sqlparser.antlr.hive.HplsqlParser;
import org.luvx.sqlparser.antlr.hive.table.HiveTableLineageModel;
import org.luvx.sqlparser.antlr.hive.table.TableNameModel;

import java.util.HashSet;
import java.util.Optional;

/**
 * @author Ren, Xie
 */
@Slf4j
public class HplsqlTableLineageVisitor extends HplsqlBaseVisitor {

    private       TableNameModel          outputTable;
    private final HashSet<TableNameModel> inputTables = new HashSet<>();

    /**
     * visitInsert获取insert的table_name节点，作为目标输出表
     *
     * @param ctx
     * @return
     */
    @Override
    public Object visitInsert_stmt(HplsqlParser.Insert_stmtContext ctx) {
        outputTable = Optional.ofNullable(ctx)
                .map(HplsqlParser.Insert_stmtContext::table_name)
                .map(RuleContext::getText)
                .map(TableNameModel::parseTableName)
                .orElse(null);
        return super.visitInsert_stmt(ctx);
    }

    /**
     * 获取from真实表，加到来源表的Set里
     *
     * @param ctx
     * @return
     */
    @Override
    public Object visitFrom_table_clause(HplsqlParser.From_table_clauseContext ctx) {
        Optional.ofNullable(ctx)
                .map(HplsqlParser.From_table_clauseContext::from_table_name_clause)
                .map(RuleContext::getText)
                .map(TableNameModel::parseTableName)
                .map(inputTables::add);
        return super.visitFrom_table_clause(ctx);
    }

    public HiveTableLineageModel getTableLineage() {
        HiveTableLineageModel hiveTableLineageModel = new HiveTableLineageModel();
        hiveTableLineageModel.setOutputTable(outputTable);
        hiveTableLineageModel.setInputTables(inputTables);
        return hiveTableLineageModel;
    }
}