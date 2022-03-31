package org.luvx.api._14;

import org.junit.jupiter.api.Test;

class Main {
    private record User(Long id, String name, Integer age) {
    }

    @Test
    public void main(String[] args) {
        User user = new User(1L, "luvx", 18);
        System.out.println(user);
    }
}
