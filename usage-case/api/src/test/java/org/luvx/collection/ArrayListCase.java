package org.luvx.collection;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Spliterator;

import static org.luvx.common.more.MorePrints.println;

/**
 * ArrayList:
 * 允许空
 * 允许重复
 * 有序
 * 非线程安全
 */
public class ArrayListCase {

    @Test
    public void run0() {
        List<String> list = new ArrayList<String>();
        /**
         * 扩容校验->grow()扩容1.5倍
         */
        list.add("1");
        list.get(0);
        list.set(0, "a");
        /**
         * 下标校验->获取旧值->移动->置空
         */
        list.remove(0);
        System.out.println(list);
    }

    /**
     * 适用于多线程环境下的方式
     */
    @Test
    public void run01() {
        List<String> list = Collections.synchronizedList(new ArrayList<>());
    }

    /**
     * 边遍历边删除尽量用迭代器
     */
    public static void method1() {
        ArrayList<String> aList = new ArrayList<>();
        aList.add("a");
        aList.add("ab");
        aList.add("abc");
        // 这一个使用for遍历不会被remove掉，
        // 因为remove第3个时，会将下面的6个往前移，
        // 第4个的下标变为2，此时i已经变为3
        aList.add("abc");
        aList.add("abcr");
        aList.add("abc");
        aList.add("abcf");
        aList.add("abc");
        aList.add("abdc");

        // 使用for循环边遍历边remove，第二个abc不会被删除
        for (int i = 0; i < aList.size(); i++) {
            if (aList.get(i).equals("abc")) {
                aList.remove(i);
            }
        }
        System.out.println(aList);

        // 可以考虑从后往前遍历并删除
        /*
        for (int i = aList.size() - 1; i >= 0; i--) {
            if (aList.get(i).equals("abc")) {
                aList.remove(i);
            }
        }
        System.out.println(aList);
        */

        // 增强for循环边遍历边删除会抛ConcurrentModificationException异常
       /*
        for (String str : aList) {
            if (str.equals("abc"))
                aList.remove(str);
        }
        */

        // 使用迭代器边遍历边remove，不会出现上述情况
        /*
        Iterator<String> iterator = aList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals("abc")) {
                iterator.remove();
            }
        }
        System.out.println(aList);
        */
    }

    @Test
    void method4() {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        Spliterator<Integer> spliterator = list.spliterator();
        // TODO
    }

    @Test
    void method5() {
        List<Integer> list = Lists.newArrayList(0, 1, 2, 3, 4, 5, 2);
        println(list.subList(1, 3));
        println(list.retainAll(List.of(2, 4)), list);
    }
}
