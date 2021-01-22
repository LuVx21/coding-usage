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
public class SelectFromSrcModel {
    /**
     * selectId+fromKey
     */
    private String          idAndFromSrc;
    /**
     * 当前查询的所在位置(父selectId + 父selectFromKey)
     * 最外层查询此字段为空
     */
    private String          parentIdAndFromSrc;
    /**
     * select字段
     */
    private List<FieldInfo> selectFields = Lists.newArrayList();
    /**
     * from 源
     * 子查询则为null
     */
    private TableInfo       fromTable;
    /**
     * from后数据来源标识
     * 本质是表别名或子查询别名
     */
    private String          fromSrcAlias;
}