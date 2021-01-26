package org.luvx.api.thread.cas;

import org.luvx.entity.User;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * CAS:原子类
 */
public class AtomicIntegerCase {

    private int num = 0;

    private static AtomicInteger integer = new AtomicInteger();

    public static void method0() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    // incrementAndGet内部的compareAndSet便是CAS操作
                    System.out.println(integer.incrementAndGet());
                }
            }
        }).start();
    }

    public static void method1() {
        User user = User.builder().id(100L).userName("foo").build();
        AtomicStampedReference<User> reference = new AtomicStampedReference(user, 100);
        user.setId(101L);
        // reference.weakCompareAndSet(user,100,);
    }

    public static void main(String[] args) {

    }
}