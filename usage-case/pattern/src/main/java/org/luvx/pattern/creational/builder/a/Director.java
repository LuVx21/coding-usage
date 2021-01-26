package org.luvx.pattern.creational.builder.a;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.luvx.pattern.creational.builder.Car;

/**
 * 该类也可以合并到Buidler抽象类中去
 * 在抽象类中提供静态build方法构想复杂对象
 */
@NoArgsConstructor
@AllArgsConstructor
public class Director {
    @Setter
    private Builder builder;

    public Car build() {
        builder.wheels("4个轮子")
                .engine("宝马引擎");
        if (!builder.isConvertible()) {
            builder.sail("玛莎拉蒂车棚");
        }
        return builder.build();
    }

    public Car build1(Builder builder) {
        builder.wheels("4个轮子")
                .engine("宝马引擎");
        if (!builder.isConvertible()) {
            builder.sail("玛莎拉蒂车棚");
        }
        return builder.build();
    }
}
