package org.luvx.sqlparser.antlr.hive.utils;

import lombok.extern.slf4j.Slf4j;
import org.luvx.sqlparser.antlr.hive.pojo.TableInfo;

/**
 * @author Ren, Xie
 */
@Slf4j
public class TableNameUtils {
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
    public static TableInfo parseTableName(String tableName) {
        String[] array = tableName.split("\\.");
        String dbName = null;
        if (array.length == 2) {
            dbName = array[0];
            tableName = array[1];
        } else if (array.length == 1) {
            tableName = array[0];
        }
        TableInfo name = new TableInfo();
        name.setDbName(dbName);
        name.setTableName(tableName);
        return name;
    }
}
