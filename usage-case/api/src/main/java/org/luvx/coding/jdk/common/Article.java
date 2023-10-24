package org.luvx.coding.jdk.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Article {
    private long      id;
    private long      userId;
    private String    articleName;
    private int       scope;
    private LocalDate pubTime;
}
