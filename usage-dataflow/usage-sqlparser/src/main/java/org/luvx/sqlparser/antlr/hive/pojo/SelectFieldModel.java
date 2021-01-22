package org.luvx.sqlparser.antlr.hive.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * 表字段信息(含表达式)
 * <pre>
 *     INSERT INTO TABLE dest.table3
 *     SELECT
 *         t1.id1 + t1.id2 as _id
 *     FROM
 *         src.table1 as t1
 *     ;
 *     存储数据为:
 *     dbName: src
 *     tableName: table1
 *     tableAlias: t1
 *     fieldName: _id
 *     alias: _id
 *     expression: t1.id1 + t1.id2
 *     fieldNames: [t1.id1, t1.id2]
 * </pre>
 *
 * @author Ren, Xie
 */
@Getter
@Setter
@ToString
public class SelectFieldModel {
    /**
     * 同一个select中不重复, 从0开始
     */
    private Integer     position;
    /**
     * 最外层查询可能重复
     */
    private String      fieldAlias;
    /**
     * 查询出的字段表达式, 可能简单也可能复杂
     */
    private String      expression;
    /**
     * expression中涉及到的字段
     * 形式为: [tableName.]fieldName
     */
    private Set<String> innerFieldNames;
}