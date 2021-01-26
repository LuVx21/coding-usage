package org.luvx.pattern.behavioral.strategy;

import java.util.Comparator;

public class ByIdComparator implements Comparator<User> {
    public int compare(User user1, User user2) {
        return (int) (user1.getId() - user2.getId());
    }
}