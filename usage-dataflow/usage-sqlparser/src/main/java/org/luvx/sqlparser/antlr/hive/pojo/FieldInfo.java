package org.luvx.sqlparser.antlr.hive.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * 表字段信息(含表达式)
 *
 * @author Ren, Xie
 */
@Getter
@Setter
@ToString
public class FieldInfo extends TableInfo {
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
        if (!super.equals(o)) {
            return false;
        }
        FieldInfo fieldInfo = (FieldInfo) o;
        return Objects.equals(fieldName, fieldInfo.fieldName) && Objects.equals(expression, fieldInfo.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fieldName, expression);
    }
}