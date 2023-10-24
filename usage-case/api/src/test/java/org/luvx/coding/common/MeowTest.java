package org.luvx.coding.common;

import org.junit.jupiter.api.Test;
import org.luvx.coding.github.common.Meow;

class MeowTest {
    @Test
    void m1() {
        Meow meow = new Meow();
        meow.setId(10000L);
        meow.setExtParams("""
                {
                "title": "haha"
                }
                """);

        String title = meow.getTitle();
        System.out.println(title);

        meow.setExtParams("""
                {
                "title": "heihei"
                }
                """);
        String title1 = meow.getTitle();
        System.out.println(title1);
    }
}