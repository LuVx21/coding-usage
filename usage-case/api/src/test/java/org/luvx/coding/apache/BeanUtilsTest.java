package org.luvx.coding.apache;

import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.Test;
import org.luvx.coding.common.more.MorePrints;
import org.luvx.coding.jdk.common.User;

public class BeanUtilsTest {

    @Test
    void m1() throws Exception {
        User user = User.builder()
                .id(100)
                .userName("foo")
                .password("bar")
                .age(18)
                .build();
        Map<String, String> map = BeanUtils.describe(user);
        BeanMap beanMap = new BeanMap(user);
        MorePrints.println(map, beanMap);

        User user1 = new User();
        BeanUtils.populate(user1, map);
        MorePrints.println(user1);
    }
}
