package org.luvx.sqlparser.antlr.hive.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
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
public class FieldInfo extends TableInfo {
    private String fieldName;

    private String      fieldAlias;
    private String      expression;
    private Set<String> fieldNames;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        FieldInfo fieldInfo = (FieldInfo) o;
        return Objects.equals(fieldName, fieldInfo.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fieldName);
    }
}