package org.luvx.app.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Ren, Xie
 */
@Data
public class DownloadData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("数字标题")
    private Double doubleData;
    @ExcelProperty("日期标题")
    private Date   date;
    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;
}
