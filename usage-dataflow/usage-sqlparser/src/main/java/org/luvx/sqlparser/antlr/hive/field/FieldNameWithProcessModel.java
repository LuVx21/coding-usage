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
public class FieldNameWithProcessModel {
    private String dbName;
    private String tableName;
    private String fieldName;
    private String process;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldNameWithProcessModel that = (FieldNameWithProcessModel) o;
        return dbName.equals(that.dbName) && tableName.equals(that.tableName) && fieldName.equals(that.fieldName) && process.equals(that.process);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbName, tableName, fieldName, process);
    }
}