package org.luvx.type;

import java.util.HashMap;

/**
 * 研究String,包装类的不可变性
 * StringBuilder的可变性
 */
public class UnChangeCase {

    public static void main(String[] args) {
        HashMap<Integer, String> map = new HashMap<>();
        Integer i = Integer.valueOf("888");
        map.put(i, "888");
        // 888
        System.out.println(map.get(i));
        i = i + 222;
        // null
        System.out.println(map.get(i));

        HashMap<String, String> map1 = new HashMap<>();
        String str = "aa";
        map1.put(str, "aaa");
        // aaa
        System.out.println(map1.get(str));
        str = str + "bb";
        // null
        System.out.println(map1.get(str));

        HashMap<StringBuilder, String> map2 = new HashMap<>();
        StringBuilder sb = new StringBuilder("aa");
        map2.put(sb, "aaa");
        // aa
        System.out.println(map2.get(sb));
        sb.append("bb");
        // aa
        System.out.println(map2.get(sb));
    }
}
