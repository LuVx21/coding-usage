package org.luvx.sqlparser.antlr.hive.table;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;

/**
 * 表血缘结构，对单个sql
 */
@Getter
@Setter
@ToString
public class HiveTableLineageModel {
    /**
     * 输出的表名
     */
    private TableNameModel          outputTable;
    /**
     * 输入的表名列表
     */
    private HashSet<TableNameModel> inputTables;
}
