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
public class HplsqlTableLineageVisitor extends HplsqlBaseVisitor<Object> {
    private       TableInfo          outputTable;
    private final HashSet<TableInfo> inputTables = new HashSet<>();

    @Override
    public Object visitCreate_table_stmt(HplsqlParser.Create_table_stmtContext ctx) {
        outputTable = Optional.ofNullable(ctx)
                .map(HplsqlParser.Create_table_stmtContext::table_name)
                .map(HplsqlParser.Table_nameContext::getText)
                .map(TableNameUtils::parseTableName)
                .orElse(null);
        return super.visitCreate_table_stmt(ctx);
    }

    @Override
    public Object visitCreate_local_temp_table_stmt(HplsqlParser.Create_local_temp_table_stmtContext ctx) {
        outputTable = Optional.ofNullable(ctx)
                .map(HplsqlParser.Create_local_temp_table_stmtContext::ident)
                .map(HplsqlParser.IdentContext::getText)
                .map(TableNameUtils::parseTableName)
                .orElse(null);
        return super.visitCreate_local_temp_table_stmt(ctx);
    }

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
        Optional<TableInfo> table = Optional.ofNullable(ctx)
                .map(HplsqlParser.From_table_name_clauseContext::table_name)
                .map(RuleContext::getText)
                .map(TableNameUtils::parseTableName);
        Optional.ofNullable(ctx)
                .map(HplsqlParser.From_table_name_clauseContext::from_alias_clause)
                .map(HplsqlParser.From_alias_clauseContext::ident)
                .map(RuleContext::getText)
                .ifPresent(a -> table.ifPresent(t -> t.setTableAlias(a)));
        inputTables.add(table.orElse(null));
        return super.visitFrom_table_name_clause(ctx);
    }

    public HiveTableLineage getTableLineage() {
        HiveTableLineage result = new HiveTableLineage();
        result.setOutputTable(outputTable);
        result.setInputTables(inputTables);
        return result;
    }
}