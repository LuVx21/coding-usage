package org.luvx.pattern.behavioral.strategy;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class UserComCase {

    @Test
    public void run01() {
        User user1 = new User("luvx", "luvx", 20);
        user1.setId(2L);
        User user2 = new User("luvx", "luvx", 21);
        user2.setId(1L);

        List<User> list = new ArrayList<>();
        list.add(user1);
        list.add(user2);

        System.out.println(UserComUtil.getComResult(list, new ByIdComparator()));
        System.out.println(UserComUtil.getComResult(list, new ByAgeComparator()));
    }
}