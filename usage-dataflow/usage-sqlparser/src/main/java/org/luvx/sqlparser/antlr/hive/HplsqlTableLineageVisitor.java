package org.luvx.sqlparser.antlr.hive;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.RuleContext;
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

    @Override
    public Object visitInsert_stmt(HplsqlParser.Insert_stmtContext ctx) {
        outputTable = Optional.ofNullable(ctx)
                .map(HplsqlParser.Insert_stmtContext::table_name)
                .map(RuleContext::getText)
                .map(TableNameModel::parseTableName)
                .orElse(null);
        return super.visitInsert_stmt(ctx);
    }

    @Override
    public Object visitFrom_table_name_clause(HplsqlParser.From_table_name_clauseContext ctx) {
        if (ctx == null) {
            return null;
        }
        inputTables.add(TableNameModel.parseTableName(ctx.table_name().getText()));
        return super.visitFrom_table_name_clause(ctx);
    }

    public HiveTableLineageModel getTableLineage() {
        HiveTableLineageModel hiveTableLineageModel = new HiveTableLineageModel();
        hiveTableLineageModel.setOutputTable(outputTable);
        hiveTableLineageModel.setInputTables(inputTables);
        return hiveTableLineageModel;
    }
}