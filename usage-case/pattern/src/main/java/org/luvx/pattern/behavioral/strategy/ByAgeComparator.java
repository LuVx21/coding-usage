package org.luvx.pattern.behavioral.strategy;

import java.util.Comparator;

public class ByAgeComparator implements Comparator<User> {
    public int compare(User user1, User user2) {
        return user1.getAge() - user2.getAge();
    }
}