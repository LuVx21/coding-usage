package org.luvx.sqlparser.antlr.hive.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
import java.util.Set;

/**
 * 表字段信息(含表达式)
 *
 * @author Ren, Xie
 */
@Getter
@Setter
@ToString
public class TableFieldInfo extends TableInfo {
    private String fieldName;

    private Integer position;
    private String  expression;

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
        TableFieldInfo fieldInfo = (TableFieldInfo) o;
        return Objects.equals(fieldName, fieldInfo.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fieldName);
    }
}