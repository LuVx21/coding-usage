package org.luvx.sqlparser.antlr.hive.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * 查询对象(一个select)的字段信息(含别名)
 * 如：select a + b as c from table;
 * 存储数据为:
 * expression:a+b
 * fieldNames:[a,b]
 * alias:c
 *
 * @author Ren, Xie
 */
@Getter
@Setter
@ToString
public class SelectFieldModel {
    private String      expression;
    private Set<String> fieldNames;
    private String      alias;
}