package org.luvx.sqlparser.antlr.hive;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.ParserRuleContext;
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
    private final String sql;

    private TableInfo          outputTable;
    private HashSet<FieldInfo> sourceFields;

    private String thisSelectId;

    /**
     * 标记是否最外层的查询字段
     */
    private Boolean         startSelectItem = false;
    private FieldInfo       selectField;
    private List<FieldInfo> selectFields;

    /**
     * selectId_alias = SelectModel
     */
    @Getter
    private final HashMap<String, SelectModel> hiveFieldSelects   = Maps.newLinkedHashMap();
    /**
     * selectId = 父selectId_alias
     */
    private final Map<Integer, String>         selectParentKeyMap = Maps.newHashMap();

    public HplsqlFieldLineageVisitor(String sql) {
        this.sql = sql;
    }

    private String substringSql(ParserRuleContext ctx) {
        return sql.substring(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex() + 1);
    }

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
            from_clauseContext
                    .map(HplsqlParser.From_clauseContext::from_table_clause)
                    .ifPresent(c -> a(thisId, c));

            from_clauseContext
                    .map(HplsqlParser.From_clauseContext::from_join_clause)
                    .orElse(Lists.newArrayList())
                    .stream()
                    .filter(Objects::nonNull)
                    .map(HplsqlParser.From_join_clauseContext::from_table_clause)
                    .forEach(c -> a(thisId, c));
        }
        return super.visitSelect_stmt(ctx);
    }

    private void a(final Integer thisId,
                   HplsqlParser.From_table_clauseContext from_table_clauseContext) {
        SelectModel selectModel = new SelectModel();
        Optional<HplsqlParser.From_table_clauseContext> fromTable = Optional.ofNullable(from_table_clauseContext);
        Optional<HplsqlParser.From_table_name_clauseContext> fromTableName = fromTable
                .map(HplsqlParser.From_table_clauseContext::from_table_name_clause);
        fromTableName
                .map(HplsqlParser.From_table_name_clauseContext::table_name)
                .map(RuleContext::getText)
                .map(TableNameUtils::parseTableName)
                .ifPresent(selectModel::setFromTable);
        fromTableName
                .map(HplsqlParser.From_table_name_clauseContext::from_alias_clause)
                .map(HplsqlParser.From_alias_clauseContext::ident)
                .map(RuleContext::getText)
                .ifPresent(selectModel::setTableAlias);
        Optional<HplsqlParser.From_subselect_clauseContext> fromSubSelect = fromTable
                .map(HplsqlParser.From_table_clauseContext::from_subselect_clause);
        fromSubSelect
                .map(HplsqlParser.From_subselect_clauseContext::from_alias_clause)
                .map(HplsqlParser.From_alias_clauseContext::ident)
                .map(RuleContext::getText)
                .ifPresent(selectModel::setTableAlias);
        final String fromKey = thisId + "_" + Optional.ofNullable(selectModel.getTableAlias()).orElse("");
        fromSubSelect
                .map(HplsqlParser.From_subselect_clauseContext::select_stmt)
                .map(HplsqlParser.Select_stmtContext::fullselect_stmt)
                .map(HplsqlParser.Fullselect_stmtContext::fullselect_stmt_item)
                .ifPresent(subSelects ->
                        subSelects.forEach(
                                item -> selectParentKeyMap.put(item.getStart().getStartIndex(), fromKey)
                        )
                );

        selectModel.setId(fromKey);
        selectModel.setParentId(selectParentKeyMap.get(thisId));
        selectModel.setSelectItems(Lists.newArrayList());
        hiveFieldSelects.put(fromKey, selectModel);
    }

    /**
     * selectItem 获取别名名，初始化selectItem存相关字段的fieldNames
     */
    @Override
    public Object visitSelect_list_item(HplsqlParser.Select_list_itemContext ctx) {
        startSelectItem = true;
        selectField = new FieldInfo();
        selectField.setFieldNames(Sets.newHashSet());
        Optional.ofNullable(ctx)
                .map(HplsqlParser.Select_list_itemContext::expr)
                .map(this::substringSql)
                .ifPresent(selectField::setExpression);
        Optional.ofNullable(ctx)
                .map(HplsqlParser.Select_list_itemContext::select_list_alias)
                .map(HplsqlParser.Select_list_aliasContext::ident)
                .map(RuleContext::getText)
                .ifPresent(selectField::setFieldAlias);
        Object visit = super.visitSelect_list_item(ctx);
        selectFields.add(selectField);
        return visit;
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
                            selectField.getFieldNames().add(TableNameUtils.dealNameMark(s));
                        }
                    });
        }
        return super.visitExpr(ctx);
    }

    /**
     * 处理每个子select进入前，
     * 初始化selectItem相关的变量
     */
    @Override
    public Object visitSubselect_stmt(HplsqlParser.Subselect_stmtContext ctx) {
        thisSelectId = ctx.getStart().getStartIndex() + "";
        selectFields = Lists.newArrayList();
        return super.visitSubselect_stmt(ctx);
    }

    /**
     * from语句，处理于所有selectItem结束
     * 对上面解析出的字段名中的表别名进行处理 如t0.field
     */
    @Override
    public Object visitFrom_clause(HplsqlParser.From_clauseContext ctx) {
        startSelectItem = false;
        final String prefix = thisSelectId + "_";
        for (FieldInfo model : selectFields) {
            HashMap<String, Set<String>> aliasMap = Maps.newHashMap();

            model.getFieldNames().forEach(fieldNames -> {
                String[] sp = fieldNames.split("\\.");
                int len = sp.length;
                String fieldName = sp[len - 1];
                if (len == 1) {
                    boolean noSubSelect = true;
                    for (String value : selectParentKeyMap.values()) {
                        if (value.startsWith(prefix)) {
                            aliasMap.computeIfAbsent(value, t -> Sets.newHashSet()).add(fieldName);
                            noSubSelect = false;
                        }
                    }
                    if (noSubSelect) {
                        aliasMap.computeIfAbsent(prefix, t -> Sets.newHashSet()).add(fieldName);
                    }
                } else if (len == 2) {
                    String key = prefix + sp[len - 2];
                    aliasMap.computeIfAbsent(key, t -> Sets.newHashSet()).add(fieldName);
                }
            });

            for (Map.Entry<String, Set<String>> entry : aliasMap.entrySet()) {
                FieldInfo temp = new FieldInfo();
                temp.setExpression(model.getExpression());
                temp.setFieldAlias(model.getFieldAlias());
                Set<String> value = entry.getValue();
                temp.setFieldNames(value);
                if (value.size() == 1 && temp.getFieldAlias() == null) {
                    temp.setFieldAlias(value.iterator().next());
                }

                SelectModel selectModel = hiveFieldSelects.get(entry.getKey());
                if (selectModel != null) {
                    List<FieldInfo> selectItems = selectModel.getSelectItems();
                    if (selectItems == null) {
                        selectModel.setSelectItems(selectItems = Lists.newArrayList());
                    }
                    selectItems.add(temp);
                }
            }
        }
        return super.visitFrom_clause(ctx);
    }

    /**
     * 获取目标字段
     * 也就是parentId为null的最外层select的字段别名
     */
    private List<FieldInfo> getTargetFields(List<SelectModel> hiveFieldSelectList) {
        List<List<String>> items = hiveFieldSelectList.stream()
                .filter(item -> item.getParentId() == null)
                .map(SelectModel::getSelectItems)
                .map(fields -> fields.stream()
                        .map(FieldInfo::getFieldAlias)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        List<String> res = Lists.newArrayList();
        items.forEach(res::addAll);
        res = res.stream().distinct().collect(Collectors.toList());
        List<FieldInfo> fieldInfos = Lists.newArrayList();
        for (String i : res) {
            FieldInfo fieldInfo = new FieldInfo();
            if (outputTable != null) {
                fieldInfo.setDbName(outputTable.getDbName());
                fieldInfo.setTableName(outputTable.getTableName());
            }
            fieldInfo.setFieldName(i);
            fieldInfos.add(fieldInfo);
        }
        return fieldInfos;
    }

    /**
     * 递归按每个字段从外到内寻找每个字段的来源
     * 逻辑为最外的字段别名，父id -> 匹配子id别名 ->
     * -> 如果是来源是表，存储，如果来源是子select，继续递归
     */
    private void findFieldSource(String fieldExpression, List<SelectModel> hiveFieldSelectList, String targetField, String parentId) {
        for (SelectModel select : hiveFieldSelectList) {
            if ((parentId == null && select.getParentId() == null) ||
                    (select.getParentId() != null && select.getParentId().equals(parentId))) {
                if (select.getSelectItems() != null) {
                    if (select.getFromTable() == null) {
                        for (FieldInfo selectItem : select.getSelectItems()) {
                            if (selectItem.getFieldAlias().equals(targetField)) {
                                if (selectItem.getExpression().length() > fieldExpression.length()) {
                                    fieldExpression = selectItem.getExpression();
                                }
                                for (String field : selectItem.getFieldNames()) {
                                    findFieldSource(fieldExpression, hiveFieldSelectList, field, select.getId());
                                }
                            }
                        }
                    } else {
                        for (FieldInfo selectItem : select.getSelectItems()) {
                            if (selectItem.getFieldAlias().equals(targetField)) {
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
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取字段血缘列表
     */
    public List<HiveFieldLineage> getHiveFieldLineage() {
        final List<SelectModel> selectList = Lists.newArrayList(hiveFieldSelects.values());
        List<HiveFieldLineage> result = Lists.newArrayList();
        String fieldExpression = "";
        getTargetFields(selectList).forEach(field -> {
            HiveFieldLineage lineage = new HiveFieldLineage();
            lineage.setField(field);
            sourceFields = Sets.newHashSet();
            findFieldSource(fieldExpression, selectList, field.getFieldName(), null);
            lineage.setSourceFields(sourceFields);
            result.add(lineage);
        });

        return result;
    }
}