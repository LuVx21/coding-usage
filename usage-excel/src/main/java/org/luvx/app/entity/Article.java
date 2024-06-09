package org.luvx.app.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class Article {
    @ExcelProperty("文章名称")
    private String name;
    @ExcelProperty("文章评分")
    private Double score;
    @ExcelProperty("发布日期")
    private Date   date;
    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;
}
