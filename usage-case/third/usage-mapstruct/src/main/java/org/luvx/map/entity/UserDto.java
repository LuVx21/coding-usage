package org.luvx.map.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
    private Long id;
    private String userName;
    private String password;
    private Integer age;
}
