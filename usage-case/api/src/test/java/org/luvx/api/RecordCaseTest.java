package org.luvx.api;

import org.junit.jupiter.api.Test;
import org.luvx.coding.common.more.MorePrints;

class RecordCaseTest {

    private record User(Long id, String name, Integer age) {
    }

    @Test
    void m1() {
        User user = new User(1L, "luvx", 18);
        MorePrints.println(user);

        if (user instanceof User(Long id, String name, Integer age)) {
            System.out.println("Name: " + name);
        }

        switch (user) {
            case User(Long id, String name, Integer age) -> {
                System.out.println("Name: " + name + ", Age: " + age);
            }
        }
    }

}
