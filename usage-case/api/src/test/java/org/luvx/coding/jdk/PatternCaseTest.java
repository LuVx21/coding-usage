package org.luvx.pattern;

import org.junit.jupiter.api.Test;
import org.luvx.coding.common.more.MoreRuns;

import java.util.regex.Pattern;

public class PatternCaseTest {
    /**
     * 1~100的数字, 支持匹配类似023这样
     */
    private static final String  ID_REGEXP  = "^(0){0,2}[1-9]$|^(0)?[1-9]\\d$|^100$";
    /**
     * 有预编译
     */
    private static final Pattern ID_PATTERN = Pattern.compile(ID_REGEXP);
    ;

    // 劣
    private void method0(String str) {
        for (int i = 0; i < 10000; i++) {
            Pattern.matches(ID_REGEXP, str);
            // 或
            // str.matches(ID_REGEXP);
        }
    }

    // 优
    private void method1(String str) {
        for (int i = 0; i < 10000; i++) {
            ID_PATTERN.matcher(str).matches();
        }
    }

    // 劣
    private void method2(String str) {
        for (int i = 0; i < 10000; i++) {
            str.replaceAll(ID_REGEXP, "LuVx");
        }
    }

    // 优
    private void method3(String str) {
        for (int i = 0; i < 10000; i++) {
            ID_PATTERN.matcher(str).replaceAll("LuVx");
        }
    }

    @Test
    void run00() {
        MoreRuns.runWithTime(() -> method0("023"));
        MoreRuns.runWithTime(() -> method1("023"));
    }

    @Test
    void run01() {
        String str = "023152345579844321685746154023206548402304568574687023054687980";
        MoreRuns.runWithTime(() -> method2(str));
        MoreRuns.runWithTime(() -> method3(str));
    }
}
