package org.luvx.pattern;

import org.luvx.common.util.Runs;

import java.util.regex.Pattern;

public class Demo2 {

    private static final String ID_REGEXP = "^(0){0,2}[1-9]$|^(0)?[1-9]\\d$|^100$";
    /**
     * 有预编译
     */
    private static final Pattern ID_PATTERN = Pattern.compile(ID_REGEXP);

    // 劣
    public static void method0(String str) {
        for (int i = 0; i < 10000; i++) {
            Pattern.matches(ID_REGEXP, str);
            // 或
            // str.matches(ID_REGEXP);
        }
    }

    // 优
    public static void method1(String str) {
        for (int i = 0; i < 10000; i++) {
            ID_PATTERN.matcher(str).matches();
        }
    }

    public static void main(String[] args) {
        String str = "045";
        Runs.runWithTime(() -> method0(str));
        Runs.runWithTime(() -> method1(str));
    }
}
