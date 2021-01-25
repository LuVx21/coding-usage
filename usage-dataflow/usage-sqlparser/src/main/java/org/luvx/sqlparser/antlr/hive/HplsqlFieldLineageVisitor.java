package org.luvx.sqlparser.antlr.hive;

import com.alibaba.fastjson.JSON;
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
import org.luvx.sqlparser.antlr.hive.pojo.HiveFieldLineage;
import org.luvx.sqlparser.antlr.hive.pojo.SelectFieldModel;
import org.luvx.sqlparser.antlr.hive.pojo.SelectFromSrcModel;
import org.luvx.sqlparser.antlr.hive.pojo.TableFieldInfo;
import org.luvx.sqlparser.antlr.hive.pojo.TableInfo;
import org.luvx.sqlparser.antlr.hive.utils.TableNameUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 基本思路:
 * 1. 分析from语句, 确定当前查询和子查询的关系
 * 2. 获取查询字段, 增加字段的位置, 别名等信息, 解析内部字段信息
 * 3. 依据selectId+fromSrc封装成对象(包括所有子查询), 其中字段全部来源自这个fromSrc
 * 4. 分析所有的3中对象, 确定整体查询的字段
 * 5. 根据4中字段找到来源
 *
 * @author Ren, Xie
 */
@Slf4j
public class HplsqlFieldLineageVisitor extends HplsqlBaseVisitor<Object> {
    private final String sql;

    private TableInfo              outputTable;
    private int                    thisSelectId;
    private int                    fieldPosition;
    /**
     * 标记是否最外层的查询字段
     */
    private Boolean                startSelectItem = false;
    private SelectFieldModel       selectField;
    private List<SelectFieldModel> selectFields;

    /**
     * selectId_FromSrc = SelectModel
     */
    @Getter
    private final Map<String, SelectFromSrcModel> hiveFieldSelects            = Maps.newLinkedHashMap();
    /**
     * 存储子查询在哪个select的哪个fromSrc
     * selectId = 父selectId_fromSrc
     */
    private final Map<Integer, String>            selectId2ParentIdAndFromSrc = Maps.newHashMap();
    /**
     * 存储查询有哪些fromSrc
     * selectId = from源
     */
    private final HashMultimap<Integer, String>   selectId2FromSrc            = HashMultimap.create();

    public HplsqlFieldLineageVisitor(String sql) {
        this.sql = sql;
    }

    private String substringSql(ParserRuleContext ctx) {
        return sql.substring(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex() + 1);
    }

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
     * 进入查询语句
     * 1. 初始化变量
     * 2. 解析from语句, 获取表及子查询的关系
     *
     * @param ctx
     * @return
     */
    @Override
    public Object visitSubselect_stmt(HplsqlParser.Subselect_stmtContext ctx) {
        thisSelectId = ctx.getStart().getStartIndex();
        fieldPosition = 0;
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
                .ifPresent(selectModel::setFromSrcAlias);
        Optional<HplsqlParser.From_subselect_clauseContext> fromSubSelect = fromTable
                .map(HplsqlParser.From_table_clauseContext::from_subselect_clause);
        fromSubSelect
                .map(HplsqlParser.From_subselect_clauseContext::from_alias_clause)
                .map(HplsqlParser.From_alias_clauseContext::ident)
                .map(RuleContext::getText)
                .ifPresent(selectModel::setFromSrcAlias);
        // 获取fromSrc 别名: alias -> 表名 -> 生成别名
        String fromSrc = Optional.ofNullable(selectModel.getFromSrcAlias())
                .orElseGet(() -> Optional.ofNullable(selectModel.getFromTable())
                        .map(s -> {
                            s.setTableAlias(s.getTableName());
                            return s;
                        })
                        .map(TableInfo::getTableName)
                        .orElse("_sub" + System.currentTimeMillis()));
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

    /**
     * select语句中select字段部分
     * 字段可能是组合的, 来源于多个数据源
     *
     * @param ctx
     * @return
     */
    @Override
    public Object visitSelect_list_item(HplsqlParser.Select_list_itemContext ctx) {
        startSelectItem = true;
        selectField = new SelectFieldModel();
        selectField.setInnerFieldNames(Sets.newHashSet());
        Optional.ofNullable(ctx)
                .map(this::substringSql)
                .ifPresent(selectField::setExpression);
        Optional.ofNullable(ctx)
                .map(HplsqlParser.Select_list_itemContext::select_list_alias)
                .map(HplsqlParser.Select_list_aliasContext::ident)
                .map(RuleContext::getText)
                .ifPresent(selectField::setFieldAlias);
        // 别名为空时 自动生成一个别名 _c+position
        if (StringUtils.isBlank(selectField.getFieldAlias())) {
            selectField.setFieldAlias("_c" + fieldPosition);
        }
        selectField.setPosition(fieldPosition++);
        selectFields.add(selectField);
        return super.visitSelect_list_item(ctx);
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

    @Override
    public Object visitSelect_list_asterisk(HplsqlParser.Select_list_asteriskContext ctx) {
        Optional.ofNullable(ctx)
                .map(RuleContext::getText)
                .ifPresent(selectField.getInnerFieldNames()::add);
        return super.visitSelect_list_asterisk(ctx);
    }

    /**
     * 对于一个查询(select开头的)
     * <pre>
     *     field:
     *     t1.id1 + t2.id2 + t2.id3 as id
     *          -> innerFieldNames: [t1.id1, t2.id2, t2.id3]
     *     concat(t1.name1, t2.name2, t2.name3) as name
     *          -> innerFieldNames: [t1.name1, t2.name2, t2.name3]
     *     ↓
     *     selectId_t1=[id1, name1]
     *     selectId_t2=[id2, id3, name2, name3]
     * </pre>
     */
    @Override
    public Object visitFrom_clause(HplsqlParser.From_clauseContext ctx) {
        startSelectItem = false;
        final String prefix = thisSelectId + "_";
        for (SelectFieldModel field : selectFields) {
            HashMap<String, Set<String>> selectIdFromSrc2FieldsMap = Maps.newHashMap();

            field.getInnerFieldNames().forEach(name -> {
                String[] sp = name.split("\\.");
                final int len = sp.length;
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

            Integer position = field.getPosition();
            String fieldAlias = field.getFieldAlias(), expression = field.getExpression();
            for (Map.Entry<String, Set<String>> entry : selectIdFromSrc2FieldsMap.entrySet()) {
                SelectFieldModel temp = new SelectFieldModel();
                Set<String> fieldNames = entry.getValue();
                temp.setInnerFieldNames(fieldNames);
                if (fieldNames.size() == 1) {
                    if (fieldAlias == null || fieldAlias.startsWith("_c")) {
                        fieldAlias = fieldNames.iterator().next();
                    }
                }
                temp.setPosition(position);
                temp.setFieldAlias(fieldAlias);
                temp.setExpression(expression);
                SelectFromSrcModel selectModel = hiveFieldSelects.get(entry.getKey());
                if (selectModel != null) {
                    selectModel.getSelectFields().add(temp);
                }
            }
        }
        return super.visitFrom_clause(ctx);
    }

    /**
     * 递归按每个字段从外到内寻找每个字段的来源
     * 逻辑为最外的字段别名，父id -> 匹配子id别名 ->
     * -> 如果是来源是表，存储，如果来源是子select，继续递归
     */
    private void findFieldSource(List<SelectFromSrcModel> hiveFieldSelectList, final String targetField,
                                 String parentId, Set<TableFieldInfo> sourceFields) {
        for (SelectFromSrcModel select : hiveFieldSelectList) {
            String parentIdAndFromSrc = select.getParentIdAndFromSrc();
            // 最外层select || 是parentId的里层查询
            boolean b = (parentIdAndFromSrc == null && parentId == null)
                    || (parentIdAndFromSrc != null && parentIdAndFromSrc.equals(parentId));
            List<SelectFieldModel> selectFields;
            if (!b || (selectFields = select.getSelectFields()) == null) {
                continue;
            }
            TableInfo fromTable = select.getFromTable();
            for (SelectFieldModel selectField : selectFields) {
                if (!Objects.equals(selectField.getFieldAlias(), targetField)) {
                    continue;
                }
                for (String field : selectField.getInnerFieldNames()) {
                    if (fromTable != null) {
                        TableFieldInfo fieldInfo = new TableFieldInfo();
                        fieldInfo.setDbName(fromTable.getDbName());
                        fieldInfo.setTableName(fromTable.getTableName());
                        fieldInfo.setFieldName(field);
                        fieldInfo.setExpression(selectField.getExpression());
                        sourceFields.add(fieldInfo);
                    } else {
                        findFieldSource(hiveFieldSelectList, field, select.getIdAndFromSrc(), sourceFields);
                    }
                }
            }
        }
    }

    /**
     * 处理*号
     * 一个*号代表一次数据源的全列
     */
    private void replaceMul(List<SelectFromSrcModel> selectList) {
    }

    /**
     * sql 检查
     * 1. select 字段是可以重复的
     * 2. 子查询的select 字段不可以重复
     *
     * @param selectList
     */
    private void sqlCheck(List<SelectFromSrcModel> selectList) {
    }

    public List<HiveFieldLineage> getHiveFieldLineage1() {
        log.debug("解析到查询及字段信息:{}", JSON.toJSONString(hiveFieldSelects));
        final List<SelectFromSrcModel> selectList = Lists.newArrayList(hiveFieldSelects.values());
        // 可能存在多个自外层select
        List<SelectFromSrcModel> collect = selectList.stream()
                .filter(item -> item.getParentIdAndFromSrc() == null)
                .collect(Collectors.toList());

        Map<Integer, Set<TableFieldInfo>> map = Maps.newHashMap();
        for (SelectFromSrcModel selectFromSrcModel : collect) {
            Map<Integer, Set<TableFieldInfo>> temp = Maps.newHashMap();
            for (SelectFieldModel field : selectFromSrcModel.getSelectFields()) {
                Integer position = field.getPosition();
                Set<TableFieldInfo> sourceFields = Sets.newHashSet();
                temp.put(position, sourceFields);
                findFieldSource(selectList, field.getFieldAlias(), null, sourceFields);
            }
            for (Map.Entry<Integer, Set<TableFieldInfo>> entry : temp.entrySet()) {
                map.computeIfAbsent(entry.getKey(), s -> Sets.newHashSet()).addAll(entry.getValue());
            }
        }

        List<HiveFieldLineage> result = Lists.newArrayList();
        for (Map.Entry<Integer, Set<TableFieldInfo>> entry : map.entrySet()) {
            TableFieldInfo fieldInfo = new TableFieldInfo();
            if (outputTable != null) {
                fieldInfo.setDbName(outputTable.getDbName());
                fieldInfo.setTableName(outputTable.getTableName());
            }
            Integer position = entry.getKey();
            fieldInfo.setPosition(position);
            fieldInfo.setFieldName("_" + position);
            result.add(new HiveFieldLineage(fieldInfo, entry.getValue()));
        }
        return result;
    }

    /**
     * 获取字段血缘列表
     */
    @Deprecated
    public List<HiveFieldLineage> getHiveFieldLineage() {
        log.debug("解析到查询及字段信息:{}", JSON.toJSONString(hiveFieldSelects));

        final List<SelectFromSrcModel> selectList = Lists.newArrayList(hiveFieldSelects.values());
        List<HiveFieldLineage> result = Lists.newArrayList();
        List<TableFieldInfo> targetFields = getTargetFields(selectList);
        targetFields.forEach(field -> {
            Set<TableFieldInfo> sourceFields = Sets.newHashSet();
            findFieldSource(selectList, field.getFieldName(), null, sourceFields);
            result.add(new HiveFieldLineage(field, sourceFields));
        });

        return result;
    }

    /**
     * 获取目标字段
     * 也就是parentId为null的最外层select的字段别名
     */
    @Deprecated
    private List<TableFieldInfo> getTargetFields(List<SelectFromSrcModel> selectFromSrcList) {
        return selectFromSrcList.stream()
                .filter(item -> item.getParentIdAndFromSrc() == null)
                .map(SelectFromSrcModel::getSelectFields)
                .flatMap(Collection::stream)
                .map(SelectFieldModel::getFieldAlias)
                .distinct()
                .map(alias -> {
                    TableFieldInfo fieldInfo = new TableFieldInfo();
                    if (outputTable != null) {
                        fieldInfo.setDbName(outputTable.getDbName());
                        fieldInfo.setTableName(outputTable.getTableName());
                    }
                    fieldInfo.setFieldName(alias);
                    return fieldInfo;
                }).collect(Collectors.toList());
    }
}