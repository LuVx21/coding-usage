package org.luvx.coding.utils;

import com.alibaba.excel.annotation.ExcelProperty;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class EasyExcelUtils {
    public static Map<Integer, List<String>> getHeader(Class<?> clazz) {
        Field[] fieldArray = FieldUtils.getFieldsWithAnnotation(clazz, ExcelProperty.class);

        Map<Integer, List<String>> header = Maps.newHashMap();
        for (int i = 0; i < fieldArray.length; i++) {
            Field field = fieldArray[i];
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            int index = excelProperty.index();
            if (index == -1) {
                index = i;
            }
            List<String> columnHeader = Lists.newArrayList(excelProperty.value());
            header.put(index, columnHeader);
        }
        Preconditions.checkState(header.size() == fieldArray.length, "excel导出,列index重复");
        return header;
    }
}
