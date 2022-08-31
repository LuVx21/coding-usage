package org.luvx.map.converter;

import java.io.Serializable;
import java.util.Map;

import io.vavr.API;

import org.junit.jupiter.api.Test;
import org.luvx.map.entity.User;
import org.luvx.map.entity.UserDto;
import org.luvx.map.entity.UserVo;

class UserConverterTest {
    UserConverter   userConverter = UserConverter.INSTANCE;
    UserVoConverter voConverter   = UserVoConverter.INSTANCE;

    @Test
    void m1() {
        UserVo vo = new UserVo();
        vo.setId(1000L);
        vo.setUserName("foo");
        vo.setPassword("bar");
        vo.setConfirmPwd("bar");
        vo.setAge(18);
        UserDto userDto = voConverter.from2To(vo);
        User user = userConverter.from2To(userDto);
        UserDto userDto1 = userConverter.to2From(user);
        UserVo userVo = voConverter.to2From(userDto1);
        API.println(userDto, user, userDto1, userVo);
    }

    @Test
    void m2() {
        Map<String, Object> map = Map.of(
                "id", 1000L,
                "userName", "foo",
                "password", "bar",
                "confirmPwd", "bar",
                "age", 18
        );
        UserDto dto = voConverter.map2To(map);
        API.println(dto);
    }
}