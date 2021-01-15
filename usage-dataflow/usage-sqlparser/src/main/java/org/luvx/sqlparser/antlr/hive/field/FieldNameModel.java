package org.luvx.sqlparser.antlr.hive.field;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * 带过程的表字段
 *
 * @author Ren, Xie
 */
@Getter
@Setter
@ToString
public class FieldNameModel {
    private String dbName;
    private String tableName;
    private String fieldName;
    private String expression;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FieldNameModel that = (FieldNameModel) o;
        return Objects.equals(dbName, that.dbName) && Objects.equals(tableName, that.tableName)
                && Objects.equals(fieldName, that.fieldName) && Objects.equals(expression, that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbName, tableName, fieldName, expression);
    }
}