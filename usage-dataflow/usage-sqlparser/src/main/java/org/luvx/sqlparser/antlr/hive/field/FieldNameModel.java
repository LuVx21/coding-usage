package org.luvx.sqlparser.antlr.hive.field;


import lombok.Getter;
import lombok.Setter;

// 字段名
@Getter
@Setter
public class FieldNameModel {
    private String dbName;
    private String tableName;
    private String fieldName;
}