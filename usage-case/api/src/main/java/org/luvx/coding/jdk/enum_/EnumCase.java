package org.luvx.coding.jdk.enum_;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 枚举类实际是一个普通类
 * 继承自java.lang.Enum,类本身是final的,即不可被继承
 * 每个枚举值则是该类的实例,并且是static final的,
 * 这也是可以用于实现单例模式的原因
 *
 * @author renxie
 */
@Getter
@AllArgsConstructor
public enum EnumCase {
    UNKNOWN(-1),
    MON(1),
    TUE(2),
    WED(3),
    THU(4),
    FRI(5),
    SAT(6),
    SUN(7);

    private final int code;

    // public abstract void say();

    public static EnumCase fromName(String name) {
        return valueOf(name);
    }

    public static EnumCase fromCode(int code) {
        return Arrays.stream(values())
                .filter(e -> e.code == code)
                .findFirst()
                .orElse(UNKNOWN);
    }
}