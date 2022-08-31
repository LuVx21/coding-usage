package org.luvx.common;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author: Ren, Xie
 * @desc:
 */
@Data
public class User {
    private Long      id;
    private String    userName;
    private Integer   age;
    private LocalDate birthday;
}
