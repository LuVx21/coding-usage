package org.luvx.sqlparser.antlr.hive.field;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

/**
 * 血缘结果结构
 */
@Getter
@Setter
public class HiveFieldLineage {
    /**
     * 目标字段
     */
    private FieldNameModel          field;
    /**
     * 来源字段列表
     */
    private HashSet<FieldNameModel> sourceFields;
}