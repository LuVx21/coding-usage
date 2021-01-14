package org.luvx.sqlparser.antlr.hive.field;

import lombok.Getter;
import lombok.Setter;
import org.luvx.sqlparser.antlr.hive.table.TableNameModel;

import java.util.List;

/**
 * 解析单个select后的结果
 *
 * @author Ren, Xie
 */
@Getter
@Setter
public class HiveFieldLineageSelectModel {
    /**
     * index
     */
    String                                id;
    /**
     * 父id，第一层select为null
     */
    String                                parentId;
    /**
     * 来源表，来源子select则为null
     */
    TableNameModel                        fromTable;
    /**
     * 表别名
     */
    String                                tableAlias;
    /**
     * select字段
     */
    List<HiveFieldLineageSelectItemModel> selectItems;
}