package org.luvx.sqlparser.antlr.hive;

import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;
import org.luvx.sqlparser.antlr.hive.field.*;
import org.luvx.sqlparser.antlr.hive.table.TableNameModel;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ren, Xie
 */
@Slf4j
public class HplsqlFieldLineageVisitor extends HplsqlBaseVisitor {

    private TableNameModel                               outputTable;
    private HashMap<String, HiveFieldLineageSelectModel> hiveFieldSelects    = new LinkedHashMap<>();
    private Map<Integer, String>                         selectParentKeyMap  = new HashMap<>();
    private String                                       thisSelectId;
    private String                                       sourceSQL;
    private List<HiveFieldLineageSelectModel> hiveFieldSelectList = new ArrayList<>();
    private HashSet<FieldNameModel>           sourceFields;
    private String                            fieldProcess        = "";

    private HiveFieldLineageSelectItemModel       selectItemModel;
    private List<HiveFieldLineageSelectItemModel> selectFields    = new ArrayList<>();
    private Boolean                               startSelectItem = false;

    public HplsqlFieldLineageVisitor(String sql) {
        this.sourceSQL = sql;
    }

    private String subSourceSql(ParserRuleContext parserRuleContext) {
        return sourceSQL.substring(
                parserRuleContext.getStart().getStartIndex(),
                parserRuleContext.getStop().getStopIndex() + 1);
    }

    /**
     * insert解析结果表
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
     * 解析select每个selectItem里用到字段
     */
    @Override
    public Object visitExpr(HplsqlParser.ExprContext ctx) {
        if (startSelectItem) {
            Optional.ofNullable(ctx)
                    .map(HplsqlParser.ExprContext::expr_atom)
                    .map(HplsqlParser.Expr_atomContext::ident)
                    .map(ParseTree::getText)
                    .ifPresent(s -> {
                        if (!StringUtils.isNumeric(s)) {
                            selectItemModel.getFieldNames().add(TableNameModel.dealNameMark(s));
                        }
                    });
        }
        return super.visitExpr(ctx);
    }

    /**
     * selectItem 获取别名名，初始化selectItem存相关字段的fieldNames
     */
    @Override
    public Object visitSelect_list_item(HplsqlParser.Select_list_itemContext ctx) {
        startSelectItem = true;
        selectItemModel = new HiveFieldLineageSelectItemModel();
        selectItemModel.setFieldNames(new HashSet<>());
        Optional.ofNullable(ctx)
                .map(HplsqlParser.Select_list_itemContext::expr)
                .map(this::subSourceSql)
                .ifPresent(selectItemModel::setProcess);
        Optional.ofNullable(ctx)
                .map(HplsqlParser.Select_list_itemContext::select_list_alias)
                .map(HplsqlParser.Select_list_aliasContext::ident)
                .map(RuleContext::getText)
                .ifPresent(selectItemModel::setAlias);
        Object visit = super.visitSelect_list_item(ctx);
        selectFields.add(selectItemModel);
        return visit;
    }

    /**
     * from语句，处理于所有selectItem结束
     * 对上面解析出的字段名中的表别名进行处理 如t0.field
     */
    @Override
    public Object visitFrom_clause(HplsqlParser.From_clauseContext ctx) {
        startSelectItem = false;
        HashMap<String, List<HiveFieldLineageSelectItemModel>> fieldItems = new HashMap<>();
        for (HiveFieldLineageSelectItemModel item : selectFields) {
            HashMap<String, HashSet<String>> aliasSet = new HashMap<>();
            for (String field : item.getFieldNames()) {
                String[] sp = field.split("\\.");
                if (sp.length == 2) {
                    String key = thisSelectId + "_" + sp[0];
                    aliasSet.computeIfAbsent(key, t -> new HashSet<>());
                    aliasSet.get(key).add(sp[1]);
                } else if (sp.length == 1) {
                    boolean flat = true;
                    for (String k : selectParentKeyMap.values()) {
                        if (k.startsWith(thisSelectId + "_")) {
                            aliasSet.computeIfAbsent(k, t -> new HashSet<>());
                            aliasSet.get(k).add(sp[0]);
                            flat = false;
                        }
                    }
                    if (flat) {
                        String key = thisSelectId + "_";
                        aliasSet.computeIfAbsent(key, t -> new HashSet<>());
                        aliasSet.get(key).add(sp[0]);
                    }
                }
            }
            for (String key : aliasSet.keySet()) {
                fieldItems.computeIfAbsent(key, k -> new ArrayList<>());
                HiveFieldLineageSelectItemModel selectItemModel = new HiveFieldLineageSelectItemModel();
                selectItemModel.setFieldNames(aliasSet.get(key));
                selectItemModel.setAlias(item.getAlias());
                selectItemModel.setProcess(item.getProcess());
                if (selectItemModel.getFieldNames().size() == 1 && selectItemModel.getAlias() == null) {
                    selectItemModel.setAlias(selectItemModel.getFieldNames().iterator().next());
                }
                fieldItems.get(key).add(selectItemModel);
            }
        }
        for (String key : fieldItems.keySet()) {
            if (hiveFieldSelects.get(key) != null) {
                hiveFieldSelects.get(key).setSelectItems(fieldItems.get(key));
            }
        }
        return super.visitFrom_clause(ctx);
    }

    /**
     * 进入select前
     * 解析每个select存信息并另存父子关系
     * 父子来源于from subSelect, join subSelect
     */
    @Override
    public Object visitSelect_stmt(HplsqlParser.Select_stmtContext ctx) {
        List<HplsqlParser.Fullselect_stmt_itemContext> selectItems = ctx.fullselect_stmt().fullselect_stmt_item();
        for (HplsqlParser.Fullselect_stmt_itemContext selectItem : selectItems) {
            HiveFieldLineageSelectModel hiveFieldLineageSelectModel = new HiveFieldLineageSelectModel();
            Integer thisId = selectItem.getStart().getStartIndex();
            HplsqlParser.Subselect_stmtContext subSelect = selectItem.subselect_stmt();
            HplsqlParser.From_table_name_clauseContext fromTableNameClause = Optional.ofNullable(subSelect)
                    .map(HplsqlParser.Subselect_stmtContext::from_clause)
                    .map(HplsqlParser.From_clauseContext::from_table_clause)
                    .map(HplsqlParser.From_table_clauseContext::from_table_name_clause)
                    .orElse(null);
            Optional.ofNullable(fromTableNameClause)
                    .map(HplsqlParser.From_table_name_clauseContext::table_name)
                    .map(RuleContext::getText)
                    .map(TableNameModel::parseTableName)
                    .ifPresent(hiveFieldLineageSelectModel::setFromTable);
            Optional.ofNullable(fromTableNameClause)
                    .map(HplsqlParser.From_table_name_clauseContext::from_alias_clause)
                    .map(HplsqlParser.From_alias_clauseContext::ident)
                    .map(RuleContext::getText)
                    .ifPresent(hiveFieldLineageSelectModel::setTableAlias);

            Optional.ofNullable(subSelect)
                    .map(HplsqlParser.Subselect_stmtContext::from_clause)
                    .map(HplsqlParser.From_clauseContext::from_table_clause)
                    .map(HplsqlParser.From_table_clauseContext::from_subselect_clause)
                    .map(HplsqlParser.From_subselect_clauseContext::from_alias_clause)
                    .map(RuleContext::getText)
                    .ifPresent(hiveFieldLineageSelectModel::setTableAlias);

            String alias = hiveFieldLineageSelectModel.getTableAlias();
            String thisKey = String.format("%s_%s", thisId, alias == null ? "" : alias);
            hiveFieldLineageSelectModel.setId(thisKey + "");
            hiveFieldLineageSelectModel.setParentId(selectParentKeyMap.get(thisId));
            hiveFieldLineageSelectModel.setSelectItems(new ArrayList<>());
            hiveFieldSelects.put(thisKey, hiveFieldLineageSelectModel);

            Optional.ofNullable(subSelect)
                    .map(HplsqlParser.Subselect_stmtContext::from_clause)
                    .map(HplsqlParser.From_clauseContext::from_table_clause)
                    .map(HplsqlParser.From_table_clauseContext::from_subselect_clause)
                    .map(HplsqlParser.From_subselect_clauseContext::select_stmt)
                    .map(HplsqlParser.Select_stmtContext::fullselect_stmt)
                    .map(HplsqlParser.Fullselect_stmtContext::fullselect_stmt_item)
                    .ifPresent(subSelects ->
                            subSelects.forEach(item ->
                                    selectParentKeyMap.put(item.getStart().getStartIndex(), thisKey)));

            List<HplsqlParser.From_join_clauseContext> fromJoinClauses = Optional.ofNullable(subSelect)
                    .map(HplsqlParser.Subselect_stmtContext::from_clause)
                    .map(HplsqlParser.From_clauseContext::from_join_clause)
                    .orElse(new ArrayList<>());
            for (HplsqlParser.From_join_clauseContext fromJoinClauseContext : fromJoinClauses) {
                HiveFieldLineageSelectModel joinSelect = new HiveFieldLineageSelectModel();
                Optional.ofNullable(fromJoinClauseContext)
                        .map(HplsqlParser.From_join_clauseContext::from_table_clause)
                        .map(HplsqlParser.From_table_clauseContext::from_table_name_clause)
                        .map(HplsqlParser.From_table_name_clauseContext::table_name)
                        .map(RuleContext::getText)
                        .map(TableNameModel::parseTableName)
                        .ifPresent(joinSelect::setFromTable);
                Optional.ofNullable(fromJoinClauseContext)
                        .map(HplsqlParser.From_join_clauseContext::from_table_clause)
                        .map(HplsqlParser.From_table_clauseContext::from_table_name_clause)
                        .map(HplsqlParser.From_table_name_clauseContext::from_alias_clause)
                        .map(HplsqlParser.From_alias_clauseContext::ident)
                        .map(RuleContext::getText)
                        .ifPresent(joinSelect::setTableAlias);

                Optional.ofNullable(fromJoinClauseContext)
                        .map(HplsqlParser.From_join_clauseContext::from_table_clause)
                        .map(HplsqlParser.From_table_clauseContext::from_subselect_clause)
                        .map(HplsqlParser.From_subselect_clauseContext::from_alias_clause)
                        .map(RuleContext::getText)
                        .ifPresent(joinSelect::setTableAlias);

                String jalias = joinSelect.getTableAlias();
                String jkey = String.format("%s_%s", thisId, jalias == null ? "" : jalias);
                joinSelect.setId(jkey);
                joinSelect.setParentId(selectParentKeyMap.get(thisId));
                joinSelect.setSelectItems(new ArrayList<>());
                hiveFieldSelects.put(jkey, joinSelect);

                Optional.ofNullable(fromJoinClauseContext)
                        .map(HplsqlParser.From_join_clauseContext::from_table_clause)
                        .map(HplsqlParser.From_table_clauseContext::from_subselect_clause)
                        .map(HplsqlParser.From_subselect_clauseContext::select_stmt)
                        .map(HplsqlParser.Select_stmtContext::fullselect_stmt)
                        .map(HplsqlParser.Fullselect_stmtContext::fullselect_stmt_item)
                        .ifPresent(subSelects ->
                                subSelects.forEach(item ->
                                        selectParentKeyMap.put(item.getStart().getStartIndex(), jkey)));
            }
        }
        return super.visitSelect_stmt(ctx);
    }

    /**
     * 处理每个子select进入前，
     * 初始化selectItem相关的变量
     */
    @Override
    public Object visitSubselect_stmt(HplsqlParser.Subselect_stmtContext ctx) {
        thisSelectId = ctx.getStart().getStartIndex() + "";
        selectFields = new ArrayList<>();
        return super.visitSubselect_stmt(ctx);
    }


    /**
     * 转换HashMap存储为List
     */
    private void transSelectToList() {
        for (String key : hiveFieldSelects.keySet()) {
            hiveFieldSelectList.add(hiveFieldSelects.get(key));
        }
    }

    /**
     * 获取目标字段
     * 也就是parentId为null的最外层select的字段别名
     */
    private List<FieldNameModel> getTargetFields() {
        List<List<String>> items = hiveFieldSelectList.stream()
                .filter(item -> item.getParentId() == null)
                .map(HiveFieldLineageSelectModel::getSelectItems)
                .map(fields -> fields.stream()
                        .map(HiveFieldLineageSelectItemModel::getAlias)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        List<String> res = new ArrayList<>();
        for (List<String> item : items) {
            res.addAll(item);
        }
        res = res.stream().distinct().collect(Collectors.toList());
        List<FieldNameModel> fieldNameModels = new ArrayList<>();
        for (String i : res) {
            FieldNameModel fieldNameModel = new FieldNameModel();
            if (outputTable != null) {
                fieldNameModel.setDbName(outputTable.getDbName());
                fieldNameModel.setTableName(outputTable.getTableName());
            }
            fieldNameModel.setFieldName(i);
            fieldNameModels.add(fieldNameModel);
        }
        return fieldNameModels;
    }

    /**
     * 递归按每个字段从外到内寻找每个字段的来源
     * 逻辑为最外的字段别名，父id -> 匹配子id别名 ->
     * -> 如果是来源是表，存储，如果来源是子select，继续递归
     */
    private void findFieldSource(String targetField, String parentId) {
        hiveFieldSelectList.forEach(select -> {
            if ((parentId == null && select.getParentId() == null) ||
                    (select.getParentId() != null && select.getParentId().equals(parentId))) {
                if (select.getSelectItems() != null) {
                    if (select.getFromTable() == null) {
                        select.getSelectItems().forEach(selectItem -> {
                            if (selectItem.getAlias().equals(targetField)) {
                                if (selectItem.getProcess().length() > fieldProcess.length()) {
                                    fieldProcess = selectItem.getProcess();
                                }
                                for (String field : selectItem.getFieldNames()) {
                                    findFieldSource(field, select.getId());
                                }
                            }
                        });
                    } else {
                        select.getSelectItems().forEach(selectItem -> {
                            if (selectItem.getAlias().equals(targetField)) {
                                if (selectItem.getProcess().length() > fieldProcess.length()) {
                                    fieldProcess = selectItem.getProcess();
                                }
                                for (String field : selectItem.getFieldNames()) {
                                    FieldNameModel fieldNameWithProcessModel = new FieldNameModel();
                                    fieldNameWithProcessModel.setDbName(select.getFromTable().getDbName());
                                    fieldNameWithProcessModel.setTableName(select.getFromTable().getTableName());
                                    fieldNameWithProcessModel.setFieldName(field);
                                    fieldNameWithProcessModel.setExpression(fieldProcess);
                                    sourceFields.add(fieldNameWithProcessModel);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 获取字段血缘列表
     */
    public List<HiveFieldLineage> getHiveFieldLineage() {
        transSelectToList();
        List<FieldNameModel> targetFields = getTargetFields();
        List<HiveFieldLineage> hiveFieldLineageModelList = new ArrayList<>();
        for (FieldNameModel field : targetFields) {
            HiveFieldLineage hiveFieldLineageModel = new HiveFieldLineage();
            hiveFieldLineageModel.setField(field);
            sourceFields = new HashSet<>();
            fieldProcess = "";
            findFieldSource(field.getFieldName(), null);
            hiveFieldLineageModel.setSourceFields(sourceFields);
            hiveFieldLineageModelList.add(hiveFieldLineageModel);
        }
        return hiveFieldLineageModelList;
    }

    /**
     * 获取sql解析处理后的结果
     */
    public HashMap<String, HiveFieldLineageSelectModel> getHiveFieldSelects() {
        return hiveFieldSelects;
    }
}