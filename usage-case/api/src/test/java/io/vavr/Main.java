package io.vavr;

import org.junit.jupiter.api.Test;

class Main {

    @Test
    void lazyTest() {
        Lazy<String> sl = Lazy.of(() -> {
            System.out.println("lazy get...");
            return "a";
        });
        String s = sl
                // .filter("a"::equals)
                // .map(s -> s + "b")
                .peek(System.out::println)
                .get();
        System.out.println(s);
    }
}
