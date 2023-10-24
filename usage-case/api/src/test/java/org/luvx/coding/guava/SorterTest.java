package org.luvx.coding.guava;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import io.vavr.Tuple;
import io.vavr.Tuple3;
import org.junit.jupiter.api.Test;

import java.util.Comparator;

/**
 * @ClassName: org.luvx.guava
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/2/3 11:26
 */
public class SorterTest {
    /**
     * 排序
     */
    private Ordering<String> byLengthOrdering = new Ordering<String>() {
        public int compare(String left, String right) {
            return Ints.compare(left.length(), right.length());
        }
    };

    /**
     * 比较
     */
    private Comparator<Tuple3<String, String, Integer>> comparator = new Comparator<Tuple3<String, String, Integer>>() {
        @Override
        public int compare(Tuple3<String, String, Integer> left, Tuple3<String, String, Integer> right) {
            return ComparisonChain.start()
                    .compare(left._1, right._1)
                    .compare(left._2, right._2)
                    .result();
        }
    };

    @Test
    public void method1() {
        Tuple3<String, String, Integer> user1 = Tuple.of("foo1", "bar", 15);
        Tuple3<String, String, Integer> user2 = Tuple.of("foo", "bar", 15);

        int result = comparator.compare(user1, user2);
        System.out.println(result);
    }
}
