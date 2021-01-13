package org.luvx.druid.lineage.pojo;

import lombok.Getter;
import lombok.Setter;
import org.luvx.druid.lineage.utils.EmptyUtils;

/**
 * @author Ren, Xie
 */
@Getter
@Setter
public class LineageField implements Comparable<LineageField> {
    private String  field;
    private String  expression;
    private String  sourceDbName;
    private String  sourceTableName;
    private String  sourceFieldName;
    private Boolean isEnd = false;

    public void setSourceTableName(String sourceTableName) {
        final String tan = ".";
        sourceTableName = EmptyUtils.isEmpty(sourceTableName) ? sourceTableName : sourceTableName.replace("`", "");
        if (sourceTableName.contains(tan)) {
            sourceDbName = sourceTableName.substring(0, sourceTableName.indexOf(tan));
            this.sourceTableName = sourceTableName.substring(sourceTableName.indexOf(tan) + 1);
        } else {
            this.sourceTableName = sourceTableName;
        }
    }

    @Override
    public int compareTo(LineageField o) {
        if (this.getField().equals(o.getField())) {
            return 0;
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LineageField myColumn = (LineageField) o;
        if (!this.getField().equals(myColumn.getField())) {
            return false;
        }
        if (EmptyUtils.isNotEmpty(sourceTableName) && !sourceTableName.equals(myColumn.sourceTableName)) {
            return false;
        }
        if (EmptyUtils.isNotEmpty(sourceFieldName)) {
            return sourceFieldName.equals(myColumn.sourceFieldName);
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = getField().hashCode();
        if (EmptyUtils.isNotEmpty(sourceTableName)) {
            result = 31 * result + sourceTableName.hashCode();
        }
        if (EmptyUtils.isNotEmpty(sourceFieldName)) {
            result = 31 * result + sourceFieldName.hashCode();
        }
        return result;
    }
}
