package org.luvx.sqlparser.antlr.hive.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;

/**
 * 表血缘结构，对单个sql
 *
 * @author Ren, Xie
 */
@Getter
@Setter
@ToString
public class HiveTableLineage {
    /**
     * 输出的表名
     */
    private TableInfo          outputTable;
    /**
     * 输入的表名列表
     */
    private HashSet<TableInfo> inputTables;
}
