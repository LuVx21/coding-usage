package org.luvx.sqlparser.antlr.hive;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.RuleContext;
import org.luvx.sqlparser.antlr.hive.pojo.HiveTableLineage;
import org.luvx.sqlparser.antlr.hive.pojo.TableInfo;
import org.luvx.sqlparser.antlr.hive.utils.TableNameUtils;

import java.util.HashSet;
import java.util.Optional;

/**
 * @author Ren, Xie
 */
@Slf4j
public class HplsqlTableLineageVisitor extends HplsqlBaseVisitor {
    private       TableInfo          outputTable;
    private final HashSet<TableInfo> inputTables = new HashSet<>();

    @Override
    public Object visitInsert_stmt(HplsqlParser.Insert_stmtContext ctx) {
        outputTable = Optional.ofNullable(ctx)
                .map(HplsqlParser.Insert_stmtContext::table_name)
                .map(RuleContext::getText)
                .map(TableNameUtils::parseTableName)
                .orElse(null);
        return super.visitInsert_stmt(ctx);
    }

    @Override
    public Object visitFrom_table_name_clause(HplsqlParser.From_table_name_clauseContext ctx) {
        if (ctx == null) {
            return null;
        }
        inputTables.add(TableNameUtils.parseTableName(ctx.table_name().getText()));
        return super.visitFrom_table_name_clause(ctx);
    }

    public HiveTableLineage getTableLineage() {
        HiveTableLineage hiveTableLineageModel = new HiveTableLineage();
        hiveTableLineageModel.setOutputTable(outputTable);
        hiveTableLineageModel.setInputTables(inputTables);
        return hiveTableLineageModel;
    }
}