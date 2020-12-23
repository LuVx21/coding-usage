package org.luvx.app.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Ren, Xie
 */
@Data
public class User {
    @ExcelProperty(value = "id", index = 0)
    private Long      id;
    @ExcelProperty(value = "姓名", index = 1)
    private String    userName;
    @ExcelProperty(value = "密码", index = 2)
    private String    password;
    @ExcelProperty(value = "年龄", index = 3)
    private Integer   age;
    @ExcelProperty(value = "生日", index = 4)
    private LocalDate birthday;
}
