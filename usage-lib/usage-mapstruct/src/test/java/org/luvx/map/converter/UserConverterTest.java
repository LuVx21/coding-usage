package org.luvx.map.converter;

import io.vavr.API;
import org.junit.jupiter.api.Test;
import org.luvx.map.entity.User;
import org.luvx.map.entity.UserDto;
import org.luvx.map.entity.UserVo;

class UserConverterTest {
    @Test
    void m1() {
        UserVo vo = new UserVo();
        vo.setId(1000L);
        vo.setUserName("foo");
        vo.setPassword("bar");
        vo.setConfirmPwd("bar");
        vo.setAge(18);
        UserDto dto = UserConverter.INSTANCE.vo2Dto(vo);
        UserVo vo1 = UserConverter.INSTANCE.dto2Vo(dto);
        User user = UserConverter.INSTANCE.dto2Entity(dto);
        API.println(dto, vo1, user);
    }
}