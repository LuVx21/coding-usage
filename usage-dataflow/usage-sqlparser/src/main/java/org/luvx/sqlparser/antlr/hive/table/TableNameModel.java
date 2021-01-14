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

    public static String dealNameMark(String name) {
        if (name.startsWith("`") && name.endsWith("`")) {
            return name.substring(1, name.length() - 1);
        } else {
            return name;
        }
    }

    public static TableNameModel parseTableName(String tableName) {
        TableNameModel tableNameModel = new TableNameModel();
        String[] splitTable = tableName.split("\\.");
        if (splitTable.length == 2) {
            tableNameModel.setDbName(splitTable[0]);
            tableNameModel.setTableName(splitTable[1]);
        } else if (splitTable.length == 1) {
            tableNameModel.setTableName(splitTable[0]);
        }
        return tableNameModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableNameModel that = (TableNameModel) o;
        return dbName.equals(that.dbName) && tableName.equals(that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbName, tableName);
    }
}

