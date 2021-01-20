package org.luvx.sqlparser.antlr.hive.pojo;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 一个select对象
 * 对from后进行拆, 多个from源分为多个对象
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
    private String          id;
    /**
     * 父id，第一层select 此字段为空
     */
    private String          parentId;
    /**
     * select字段
     */
    private List<FieldInfo> selectItems = Lists.newArrayList();
    /**
     * from 源
     * 子查询则为null
     */
    private TableInfo       fromTable;
    /**
     * 表别名或子查询别名
     */
    private String          tableAlias;
}