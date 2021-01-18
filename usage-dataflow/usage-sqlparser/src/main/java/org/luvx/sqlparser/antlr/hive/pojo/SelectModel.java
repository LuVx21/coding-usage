package org.luvx.sqlparser.antlr.hive.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 查询(一个select)对象
 *
 * @author Ren, Xie
 */
@Getter
@Setter
@ToString
public class SelectModel {
    /**
     * index
     */
    private String                id;
    /**
     * 父id，第一层select 此字段为空
     */
    private String                parentId;
    /**
     * select字段
     */
    private List<SelectItemModel> selectItems;
    /**
     * 来源表，来源子select则为null
     */
    private TableInfo             fromTable;
    /**
     * 表别名
     */
    private String                tableAlias;
}