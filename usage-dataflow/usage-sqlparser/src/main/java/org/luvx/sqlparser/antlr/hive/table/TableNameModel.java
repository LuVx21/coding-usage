package org.luvx.sqlparser.antlr.hive.table;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author Ren, Xie
 */
@Getter
@Setter
public class TableNameModel {
    private String dbName;
    private String tableName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableNameModel that = (TableNameModel) o;
        return Objects.equals(dbName, that.dbName) && Objects.equals(tableName, that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbName, tableName);
    }

    /**
     * 删除表名前后的`字符
     *
     * @param name
     * @return
     */
    public static String dealNameMark(String name) {
        return name.startsWith("`") && name.endsWith("`") ?
                name.substring(1, name.length() - 1) : name;
    }

    /**
     * tableName
     * dbName.tableName
     *
     * @param tableName 表名(可能含有库名)
     * @return
     */
    public static TableNameModel parseTableName(String tableName) {
        String[] array = tableName.split("\\.");
        String dbName = null;
        if (array.length == 2) {
            dbName = array[0];
            tableName = array[1];
        } else if (array.length == 1) {
            tableName = array[0];
        }
        TableNameModel name = new TableNameModel();
        name.setDbName(dbName);
        name.setTableName(tableName);
        return name;
    }
}

