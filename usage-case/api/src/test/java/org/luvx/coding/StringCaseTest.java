package org.luvx.coding;

import org.junit.jupiter.api.Test;
import org.luvx.coding.common.more.MorePrints;

import java.text.MessageFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

class StringCaseTest {

    /**
     * String较快的特殊情况
     */
    @Test
    void stringTest() {
        // 没有字符串运算,本身就是"a b c"
        String s = "a " + "b " + "c";
        String ss = "a b c";
        String sss = new String("a b c");
        System.out.println(s == ss);// true
        System.out.println(ss == sss);// false

        // since jdk7
        String s0 = new StringBuilder("a ").append("b c").toString();
        String s1 = new StringBuilder("a ").append("b c1").toString();
        System.out.println(s0.intern() == s0);// false(常量池中存在),函数返回其实是s
        System.out.println(s1.intern() == s1);// true(常量池中不存在),jdk7之前为false
    }


    @Test
    void stringTest1() {
        StringBuilder sb = new StringBuilder();
        System.out.println(sb.toString().equals(""));
    }


    /**
     * StringBuffer
     * 线程安全,方法有synchronized
     */
    @Test
    void StringBufferTest() {
        // 初始容量为16的char型数组,同时计数已使用了的位置数量
        StringBuffer sb = new StringBuffer();
        // 扩容检查,变为原来的2倍+2
        sb.append("ab");
        // 下标检查->容量确认
        sb.replace(1, 2, "c");
        // 下标检查->返回值
        sb.charAt(1);
        // 下标检查->删除长度>0进行删除
        sb.delete(1, 2);
    }


    /**
     * StringBuilder
     * 线程不安全,方法无synchronized
     * 基本同StringBuffer,方法内部都是调用
     * AbstractStringBuilder的实现
     */
    @Test
    void StringBuilderTest() {
        StringBuilder sb = new StringBuilder();
        sb.append("ab");
        sb.replace(1, 2, "c");
        sb.charAt(1);
        sb.delete(1, 2);
    }

    /**
     * `==`,equals(),hashCode()
     */
    @Test
    void equalsTest() {
        String aa = "aa";
        String cc = "aa";
        // true
        System.out.println(aa == cc);
        // true
        System.out.println(aa.equals(cc));
        // true
        System.out.println(aa.hashCode() == cc.hashCode());


        aa = new String("aa");
        cc = new String("aa");
        // false
        System.out.println(aa == cc);
        System.out.println(aa.equals(cc));
        System.out.println(aa.hashCode() == cc.hashCode());
    }

    @Test
    void method() {
        String result = MessageFormat.format("from {0} to {1};", 10001 + "", 20000);
        String b = MessageFormat.format("{0},{1},{2},''{3}'','{4}'", "a", "b", "c", "d", "e");
        MorePrints.println(result, b);
    }

    @Test
    void m1() {
        List<String> list = List.of("a", "", "b", "", "c", "");
        String join = String.join(",", list);
        System.out.println(join);
        int i = "102".compareTo("101");
        System.out.println(i);
    }

    @Test
    void m2() {
        String name = "Joan";
        String info = STR. "My name is \{ name }" ;

        System.out.println(info);

        String time = STR. "The time is \{
                // The java.time.format package is very useful
                DateTimeFormatter
                        .ofPattern("HH:mm:ss")
                        .format(LocalTime.now())
                } right now" ;
        System.out.println(time);

        int x = 10, y = 20;
        String s = STR. "\{ x } + \{ y } = \{ x + y }" ;
        System.out.println(s);
    }
}
