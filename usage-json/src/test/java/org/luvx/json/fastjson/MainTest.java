package org.luvx.json.fastjson;

import org.junit.Test;
import org.luvx.common.User;
import org.luvx.json.Json;
import org.luvx.json.base.BaseJsonMapper;

import java.time.LocalDate;
import java.util.Map;

public class MainTest {
    @Test
    public void a() {
        User u = new User();
        u.setId(1001L);
        u.setUserName("foo");
        u.setAge(18);
        u.setBirthday(LocalDate.now());

        BaseJsonMapper of = Json.FAST_JSON.of();
        String s = of.toJsonString(u);
        System.out.println(s);

        Map map = of.toMap(s);
        System.out.println(map);
    }
}