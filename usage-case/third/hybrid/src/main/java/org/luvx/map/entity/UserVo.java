package org.luvx.map.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserVo {
    private Long id;
    private String userName;
    private String password;
    private String confirmPwd;
    private Integer age;
}
