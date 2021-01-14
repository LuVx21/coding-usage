package org.luvx.sqlparser.antlr.hive.field;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * 解析单个select中存储字段和别名
 * 如：select a+b as c from table;
 * 存储数据为 fieldNames:[a,b] alias:c process:a+b
 *
 * @author Ren, Xie
 */
@Getter
@Setter
public class HiveFieldLineageSelectItemModel {
    private Set<String> fieldNames;
    private String      alias;
    private String      process;
}