package org.luvx.api.Java8.Lambda;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.luvx.coding.common.enums.EnumHasCode;

@ToString
@AllArgsConstructor
public enum Gender implements EnumHasCode<Integer> {
    MALE(1, "男"),
    FEMALE(2, "女"),
    ;
    private final int    code;
    @Getter
    private final String name;

    @Override
    public Integer getCode() {
        return code;
    }


    public static void main(String[] args) {
        System.out.println(FEMALE.toString());
    }
}
