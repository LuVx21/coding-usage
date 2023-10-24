package org.luvx.coding.jdk.concurrent.lock;

import lombok.Data;
import sun.misc.Unsafe;
import java.lang.reflect.Field;

public class UnsafeUtil {
    public static Unsafe getUnsafe() throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        return (Unsafe) theUnsafe.get(null);
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Unsafe unsafe = UnsafeUtil.getUnsafe();
        a(unsafe);
        b(unsafe);
    }

    private static void a(Unsafe unsafe) throws NoSuchFieldException {
        Person person = new Person();
        Field age = Person.class.getDeclaredField("age");
        long offset = unsafe.objectFieldOffset(age);

        System.out.println(unsafe.compareAndSwapInt(person, offset, 10, 20));
        System.out.println(person.getAge());
        System.out.println(unsafe.compareAndSwapInt(person, offset, 0, 20));
        System.out.println(person.getAge());
    }

    private static void b(Unsafe unsafe) throws NoSuchFieldException {
        Field weight = Person.class.getDeclaredField("weight");
        long staticFieldOffset = unsafe.staticFieldOffset(weight);
        System.out.println(unsafe.compareAndSwapInt(Person.class, staticFieldOffset, 10, 20));
        System.out.println(Person.weight);
        System.out.println(unsafe.compareAndSwapInt(Person.class, staticFieldOffset, 0, 20));
        System.out.println(Person.weight);
    }

    @Data
    static class Person {
        static  int weight = 0;
        private int age    = 0;
    }
}
