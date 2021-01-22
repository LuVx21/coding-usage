package org.luvx.sqlparser.antlr.hive.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * 血缘结果结构
 *
 * @author Ren, Xie
 */
@Getter
@Setter
public class HiveFieldLineage {
    /**
     * 目标字段
     */
    private TableFieldInfo      field;
    /**
     * 来源字段列表
     */
    private Set<TableFieldInfo> sourceFields;

    public HiveFieldLineage() {
    }

    public HiveFieldLineage(TableFieldInfo field, Set<TableFieldInfo> sourceFields) {
        this.field = field;
        this.sourceFields = sourceFields;
    }
}