package org.luvx.concurrent;

import org.junit.jupiter.api.Test;
import org.luvx.coding.jdk.concurrent.callback.callback.Callback1;

import java.util.Arrays;

public class CallbackTest1 {
    @Test
    public void funtest() {
        Callback1 s1 = new Callback1(2, "a", 18, 89);
        Callback1 s2 = new Callback1(3, "x", 22, 94);
        Callback1 s3 = new Callback1(1, "w", 19, 78);
        Callback1[] arrs = {s1, s2, s3};
        Arrays.sort(arrs);
        for (Callback1 student : arrs) {
            System.out.println(student);
        }
    }
}
