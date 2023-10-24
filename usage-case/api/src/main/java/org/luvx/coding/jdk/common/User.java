package org.luvx.coding.jdk.common;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    public  long          id;
    /**
     * 驼峰式命名
     */
    public  String        userName;
    /**
     * 非驼峰式命名
     */
    public  String        password;
    public  int           age;
    private LocalDate     birthday;
    private List<Article> articleList;

    public User(String userName, String password, int age) {
        this.userName = userName;
        this.password = password;
        this.age = age;
    }

    public User(long id, String userName, String password, int age) {
        this(userName, password, age);
        this.id = id;
    }
}
