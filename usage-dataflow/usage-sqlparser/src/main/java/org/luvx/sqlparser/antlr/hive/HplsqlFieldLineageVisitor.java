package org.luvx.sqlparser.antlr.hive;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;
import org.luvx.sqlparser.antlr.hive.pojo.*;
import org.luvx.sqlparser.antlr.hive.utils.TableNameUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ren, Xie
 */
@Slf4j
public class HplsqlFieldLineageVisitor extends HplsqlBaseVisitor<Object> {

    private TableInfo          outputTable;
    private HashSet<FieldInfo> sourceFields;

    private String thisSelectId;
    private String fieldExpression = "";

    private SelectItemModel       selectItemModel;
    private List<SelectItemModel> selectFields    = Lists.newArrayList();
    /**
     * 标记是否最外层的查询字段
     */
    private Boolean               startSelectItem = false;

    private final HashMap<String, SelectModel> hiveFieldSelects    = Maps.newLinkedHashMap();
    /**
     * 本select id = alias 父select id _ alias
     */
    private final Map<Integer, String>         selectParentKeyMap  = Maps.newHashMap();
    private final List<SelectModel>            hiveFieldSelectList = Lists.newArrayList();

    /**
     * insert解析结果表
     */
    @Override
    public Object visitInsert_stmt(HplsqlParser.Insert_stmtContext ctx) {
        outputTable = Optional.ofNullable(ctx)
                .map(HplsqlParser.Insert_stmtContext::table_name)
                .map(RuleContext::getText)
                .map(TableNameUtils::parseTableName)
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
                            selectItemModel.getFieldNames().add(TableNameUtils.dealNameMark(s));
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
        selectItemModel = new SelectItemModel();
        selectItemModel.setFieldNames(new HashSet<>());
        Optional.ofNullable(ctx)
                .map(HplsqlParser.Select_list_itemContext::expr)
                .map(RuleContext::getText)
                .ifPresent(selectItemModel::setExpression);
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
        HashMap<String, List<SelectItemModel>> fieldItems = new HashMap<>();
        for (SelectItemModel selectField : selectFields) {
            HashMap<String, Set<String>> aliasMap = new HashMap<>();

            for (String fieldNames : selectField.getFieldNames()) {
                String[] sp = fieldNames.split("\\.");
                int len = sp.length;
                String fieldName = sp[len - 1];
                String prefix = thisSelectId + "_";
                if (len == 1) {
                    boolean flag = true;
                    for (String value : selectParentKeyMap.values()) {
                        if (value.startsWith(prefix)) {
                            aliasMap.computeIfAbsent(value, t -> Sets.newHashSet()).add(fieldName);
                            flag = false;
                        }
                    }
                    if (flag) {
                        aliasMap.computeIfAbsent(prefix, t -> Sets.newHashSet()).add(fieldName);
                    }
                } else if (len == 2) {
                    String key = prefix + sp[len - 2];
                    aliasMap.computeIfAbsent(key, t -> Sets.newHashSet()).add(fieldName);
                }
            }

            for (Map.Entry<String, Set<String>> entry : aliasMap.entrySet()) {
                SelectItemModel temp = new SelectItemModel();
                temp.setFieldNames(entry.getValue());
                temp.setAlias(selectField.getAlias());
                temp.setExpression(selectField.getExpression());
                if (temp.getFieldNames().size() == 1 && temp.getAlias() == null) {
                    temp.setAlias(temp.getFieldNames().iterator().next());
                }

                fieldItems.computeIfAbsent(entry.getKey(), k -> Lists.newArrayList()).add(temp);
            }
        }
        for (Map.Entry<String, List<SelectItemModel>> entry : fieldItems.entrySet()) {
            SelectModel selectModel = hiveFieldSelects.get(entry.getKey());
            if (selectModel != null) {
                selectModel.setSelectItems(entry.getValue());
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
            final Integer thisId = selectItem.getStart().getStartIndex();
            Optional<HplsqlParser.From_clauseContext> from_clauseContext = Optional.of(selectItem)
                    .map(HplsqlParser.Fullselect_stmt_itemContext::subselect_stmt)
                    .map(HplsqlParser.Subselect_stmtContext::from_clause);
            Optional<HplsqlParser.From_table_clauseContext> from_table_clauseContext0 = from_clauseContext
                    .map(HplsqlParser.From_clauseContext::from_table_clause);
            a(thisId, from_table_clauseContext0);

            List<HplsqlParser.From_join_clauseContext> fromJoinClauses = from_clauseContext
                    .map(HplsqlParser.From_clauseContext::from_join_clause)
                    .orElse(new ArrayList<>());
            for (HplsqlParser.From_join_clauseContext fromJoinClauseContext : fromJoinClauses) {
                Optional<HplsqlParser.From_table_clauseContext> from_table_clauseContext1 = Optional.ofNullable(fromJoinClauseContext)
                        .map(HplsqlParser.From_join_clauseContext::from_table_clause);
                a(thisId, from_table_clauseContext1);
            }
        }
        return super.visitSelect_stmt(ctx);
    }

    private void a(Integer thisId,
                   Optional<HplsqlParser.From_table_clauseContext> from_table_clauseContext0) {
        SelectModel selectModel0 = new SelectModel();
        Optional<HplsqlParser.From_table_name_clauseContext> from_table_name_clauseContext0 = from_table_clauseContext0
                .map(HplsqlParser.From_table_clauseContext::from_table_name_clause);
        from_table_name_clauseContext0
                .map(HplsqlParser.From_table_name_clauseContext::table_name)
                .map(RuleContext::getText)
                .map(TableNameUtils::parseTableName)
                .ifPresent(selectModel0::setFromTable);
        from_table_name_clauseContext0
                .map(HplsqlParser.From_table_name_clauseContext::from_alias_clause)
                .map(HplsqlParser.From_alias_clauseContext::ident)
                .map(RuleContext::getText)
                .ifPresent(selectModel0::setTableAlias);
        Optional<HplsqlParser.From_subselect_clauseContext> from_subselect_clauseContext0 = from_table_clauseContext0
                .map(HplsqlParser.From_table_clauseContext::from_subselect_clause);
        from_subselect_clauseContext0
                .map(HplsqlParser.From_subselect_clauseContext::from_alias_clause)
                .map(RuleContext::getText)
                .ifPresent(selectModel0::setTableAlias);

        String alias = selectModel0.getTableAlias();
        String thisKey0 = String.format("%s_%s", thisId, alias == null ? "" : alias);
        selectModel0.setId(thisKey0);
        selectModel0.setParentId(selectParentKeyMap.get(thisId));
        selectModel0.setSelectItems(new ArrayList<>());
        hiveFieldSelects.put(thisKey0, selectModel0);

        from_subselect_clauseContext0
                .map(HplsqlParser.From_subselect_clauseContext::select_stmt)
                .map(HplsqlParser.Select_stmtContext::fullselect_stmt)
                .map(HplsqlParser.Fullselect_stmtContext::fullselect_stmt_item)
                .ifPresent(subSelects ->
                        subSelects.forEach(item ->
                                selectParentKeyMap.put(item.getStart().getStartIndex(), thisKey0)));
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
    private List<FieldInfo> getTargetFields() {
        List<List<String>> items = hiveFieldSelectList.stream()
                .filter(item -> item.getParentId() == null)
                .map(SelectModel::getSelectItems)
                .map(fields -> fields.stream()
                        .map(SelectItemModel::getAlias)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        List<String> res = new ArrayList<>();
        for (List<String> item : items) {
            res.addAll(item);
        }
        res = res.stream().distinct().collect(Collectors.toList());
        List<FieldInfo> fieldNameModels = new ArrayList<>();
        for (String i : res) {
            FieldInfo fieldNameModel = new FieldInfo();
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
                                if (selectItem.getExpression().length() > fieldExpression.length()) {
                                    fieldExpression = selectItem.getExpression();
                                }
                                for (String field : selectItem.getFieldNames()) {
                                    findFieldSource(field, select.getId());
                                }
                            }
                        });
                    } else {
                        select.getSelectItems().forEach(selectItem -> {
                            if (selectItem.getAlias().equals(targetField)) {
                                if (selectItem.getExpression().length() > fieldExpression.length()) {
                                    fieldExpression = selectItem.getExpression();
                                }
                                for (String field : selectItem.getFieldNames()) {
                                    FieldInfo fieldInfo = new FieldInfo();
                                    fieldInfo.setDbName(select.getFromTable().getDbName());
                                    fieldInfo.setTableName(select.getFromTable().getTableName());
                                    fieldInfo.setFieldName(field);
                                    fieldInfo.setExpression(fieldExpression);
                                    sourceFields.add(fieldInfo);
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
        List<FieldInfo> targetFields = getTargetFields();
        List<HiveFieldLineage> hiveFieldLineageModelList = new ArrayList<>();
        for (FieldInfo field : targetFields) {
            HiveFieldLineage hiveFieldLineageModel = new HiveFieldLineage();
            hiveFieldLineageModel.setField(field);
            sourceFields = new HashSet<>();
            fieldExpression = "";
            findFieldSource(field.getFieldName(), null);
            hiveFieldLineageModel.setSourceFields(sourceFields);
            hiveFieldLineageModelList.add(hiveFieldLineageModel);
        }
        return hiveFieldLineageModelList;
    }

    /**
     * 获取sql解析处理后的结果
     */
    public HashMap<String, SelectModel> getHiveFieldSelects() {
        return hiveFieldSelects;
    }
}