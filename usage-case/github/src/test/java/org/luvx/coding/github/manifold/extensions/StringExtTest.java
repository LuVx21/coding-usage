package org.luvx.coding.github.manifold.extensions;

import org.junit.jupiter.api.Test;

class StringExtTest {
    @Test
    void m1() {
        String s = "a,b,c,d";
        String[] split = s.split(',');
        split.toString().println();
    }
}