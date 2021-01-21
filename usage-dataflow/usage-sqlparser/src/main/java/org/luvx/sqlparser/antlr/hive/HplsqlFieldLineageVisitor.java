package org.luvx.sqlparser.antlr.hive;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;
import org.luvx.sqlparser.antlr.hive.pojo.FieldInfo;
import org.luvx.sqlparser.antlr.hive.pojo.HiveFieldLineage;
import org.luvx.sqlparser.antlr.hive.pojo.SelectFromSrcModel;
import org.luvx.sqlparser.antlr.hive.pojo.TableInfo;
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

    private int thisSelectId;

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
    private final HashMap<String, SelectFromSrcModel> hiveFieldSelects            = Maps.newLinkedHashMap();
    /**
     * selectId = 父selectId_alias
     */
    private final Map<Integer, String>                selectId2ParentIdAndFromSrc = Maps.newHashMap();
    /**
     * selectId = from源
     */
    private final HashMultimap<Integer, String>       selectId2FromSrc            = HashMultimap.create();

    public HplsqlFieldLineageVisitor(String sql) {
        this.sql = sql;
    }

    private String substringSql(ParserRuleContext ctx) {
        return sql.substring(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex() + 1);
    }

    @Override
    public Object visitCreate_table_stmt(HplsqlParser.Create_table_stmtContext ctx) {
        // TODO
        return super.visitCreate_table_stmt(ctx);
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

    @Override
    public Object visitSubselect_stmt(HplsqlParser.Subselect_stmtContext ctx) {
        thisSelectId = ctx.getStart().getStartIndex();
        selectFields = Lists.newArrayList();

        Optional<HplsqlParser.From_clauseContext> from_clauseContext = Optional.ofNullable(ctx)
                .map(HplsqlParser.Subselect_stmtContext::from_clause);
        from_clauseContext
                .map(HplsqlParser.From_clauseContext::from_table_clause)
                .ifPresent(this::a);

        from_clauseContext
                .map(HplsqlParser.From_clauseContext::from_join_clause)
                .orElse(Lists.newArrayList())
                .stream()
                .filter(Objects::nonNull)
                .map(HplsqlParser.From_join_clauseContext::from_table_clause)
                .forEach(this::a);

        return super.visitSubselect_stmt(ctx);
    }

    private void a(HplsqlParser.From_table_clauseContext from_table_clauseContext) {
        SelectFromSrcModel selectModel = new SelectFromSrcModel();
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
        String fromSrc = Optional.ofNullable(selectModel.getTableAlias())
                .orElseGet(() -> selectModel.getFromTable().getTableName());
        selectId2FromSrc.put(thisSelectId, fromSrc);
        final String selectIdFromSrc = thisSelectId + "_" + fromSrc;
        fromSubSelect
                .map(HplsqlParser.From_subselect_clauseContext::select_stmt)
                .map(HplsqlParser.Select_stmtContext::fullselect_stmt)
                .map(HplsqlParser.Fullselect_stmtContext::fullselect_stmt_item)
                .ifPresent(subSelects -> subSelects.forEach(item ->
                        selectId2ParentIdAndFromSrc.put(item.getStart().getStartIndex(), selectIdFromSrc)
                ));

        selectModel.setIdAndFromSrc(selectIdFromSrc);
        selectModel.setParentIdAndFromSrc(selectId2ParentIdAndFromSrc.get(thisSelectId));
        hiveFieldSelects.put(selectIdFromSrc, selectModel);
    }

    @Override
    public Object visitSelect_list_item(HplsqlParser.Select_list_itemContext ctx) {
        startSelectItem = true;
        selectField = new FieldInfo();
        selectField.setInnerFieldNames(Sets.newHashSet());
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
                            selectField.getInnerFieldNames().add(TableNameUtils.dealNameMark(s));
                        }
                    });
        }
        return super.visitExpr(ctx);
    }

    /**
     * field: t1.id1 + t2.id2 as id
     * ↓
     * selectId_t1=id1
     * selectId_t2=id2
     */
    @Override
    public Object visitFrom_clause(HplsqlParser.From_clauseContext ctx) {
        startSelectItem = false;
        final String prefix = thisSelectId + "_";
        for (FieldInfo field : selectFields) {
            HashMap<String, Set<String>> selectIdFromSrc2FieldsMap = Maps.newHashMap();

            field.getInnerFieldNames().forEach(name -> {
                String[] sp = name.split("\\.");
                int len = sp.length;
                String fieldName = sp[len - 1];
                if (len == 1) {
                    Set<String> fromSrcSet = selectId2FromSrc.get(thisSelectId);
                    if (fromSrcSet.size() > 1) {
                        throw new RuntimeException("未标识表的字段存在复数个数据源");
                    }
                    String fromSrc = fromSrcSet.iterator().next();
                    selectIdFromSrc2FieldsMap.computeIfAbsent(prefix + fromSrc, t -> Sets.newHashSet())
                            .add(fieldName);
                } else if (len == 2) {
                    String key = prefix + sp[len - 2];
                    selectIdFromSrc2FieldsMap.computeIfAbsent(key, t -> Sets.newHashSet()).add(fieldName);
                }
            });

            for (Map.Entry<String, Set<String>> entry : selectIdFromSrc2FieldsMap.entrySet()) {
                FieldInfo temp = new FieldInfo();
                Set<String> value = entry.getValue();
                temp.setInnerFieldNames(value);
                String fieldAlias = field.getFieldAlias();
                if (value.size() == 1 && fieldAlias == null) {
                    fieldAlias = value.iterator().next();
                }
                temp.setFieldAlias(fieldAlias);
                temp.setExpression(field.getExpression());
                SelectFromSrcModel selectModel = hiveFieldSelects.get(entry.getKey());
                if (selectModel != null) {
                    selectModel.getSelectItems().add(temp);
                }
            }
        }
        return super.visitFrom_clause(ctx);
    }

    /**
     * 获取目标字段
     * 也就是parentId为null的最外层select的字段别名
     */
    private List<FieldInfo> getTargetFields(List<SelectFromSrcModel> selectFromSrcList) {
        return selectFromSrcList.stream()
                .filter(item -> item.getParentIdAndFromSrc() == null)
                .map(SelectFromSrcModel::getSelectItems)
                .flatMap(Collection::stream)
                .map(FieldInfo::getFieldAlias)
                .distinct()
                .map(name -> {
                    FieldInfo fieldInfo = new FieldInfo();
                    if (outputTable != null) {
                        fieldInfo.setDbName(outputTable.getDbName());
                        fieldInfo.setTableName(outputTable.getTableName());
                    }
                    fieldInfo.setFieldName(name);
                    return fieldInfo;
                }).collect(Collectors.toList());
    }

    /**
     * 递归按每个字段从外到内寻找每个字段的来源
     * 逻辑为最外的字段别名，父id -> 匹配子id别名 ->
     * -> 如果是来源是表，存储，如果来源是子select，继续递归
     */
    private void findFieldSource(String fieldExpression, List<SelectFromSrcModel> hiveFieldSelectList, String targetField, String parentId) {
        for (SelectFromSrcModel select : hiveFieldSelectList) {
            if ((parentId == null && select.getParentIdAndFromSrc() == null) ||
                    (select.getParentIdAndFromSrc() != null && select.getParentIdAndFromSrc().equals(parentId))) {
                if (select.getSelectItems() != null) {
                    if (select.getFromTable() == null) {
                        for (FieldInfo selectItem : select.getSelectItems()) {
                            if (selectItem.getFieldAlias().equals(targetField)) {
                                if (selectItem.getExpression().length() > fieldExpression.length()) {
                                    fieldExpression = selectItem.getExpression();
                                }
                                for (String field : selectItem.getInnerFieldNames()) {
                                    findFieldSource(fieldExpression, hiveFieldSelectList, field, select.getIdAndFromSrc());
                                }
                            }
                        }
                    } else {
                        for (FieldInfo selectItem : select.getSelectItems()) {
                            if (selectItem.getFieldAlias().equals(targetField)) {
                                if (selectItem.getExpression().length() > fieldExpression.length()) {
                                    fieldExpression = selectItem.getExpression();
                                }
                                for (String field : selectItem.getInnerFieldNames()) {
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
        final List<SelectFromSrcModel> selectList = Lists.newArrayList(hiveFieldSelects.values());
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