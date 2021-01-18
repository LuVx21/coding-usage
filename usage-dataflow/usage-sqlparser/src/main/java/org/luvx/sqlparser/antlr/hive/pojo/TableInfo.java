package org.luvx.sqlparser.antlr.hive.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * 表信息
 *
 * @author Ren, Xie
 */
@Getter
@Setter
@ToString
public class TableInfo {
    protected String dbName;
    protected String tableName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableInfo that = (TableInfo) o;
        return Objects.equals(dbName, that.dbName) && Objects.equals(tableName, that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbName, tableName);
    }
}

