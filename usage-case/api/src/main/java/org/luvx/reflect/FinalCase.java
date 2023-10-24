package org.luvx.reflect;

import static java.lang.reflect.Modifier.FINAL;

import java.lang.reflect.Field;

import org.luvx.coding.common.more.MorePrints;
import org.luvx.coding.jdk.common.User;

import lombok.SneakyThrows;

public class FinalCase {
    public static final User USER = new User("foo", "bar", 18);

    /**
     * 高版本不可用
     */
    @SneakyThrows
    public static void main(String[] args) {
        MorePrints.println(USER);
        Class<FinalCase> clazz = FinalCase.class;
        Field field = clazz.getDeclaredField("USER");
        field.setAccessible(true);
        Field modifiers = field.getClass().getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & ~FINAL);
        field.set(null, new User("foo1", "bar1", 19));
        MorePrints.println(USER);
    }
}
