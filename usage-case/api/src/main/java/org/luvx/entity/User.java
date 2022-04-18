package org.luvx.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 使用redis的pojo类
 * <p>
 * 需要实现Serializable接口
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    public long   id;
    /**
     * 驼峰式命名
     */
    public String userName;
    /**
     * 非驼峰式命名
     */
    public String password;
    public int    age;

    public User(String userName, String password, int age) {
        this.userName = userName;
        this.password = password;
        this.age = age;
    }
}
