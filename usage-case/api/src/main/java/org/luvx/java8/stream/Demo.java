package org.luvx.java8.stream;

import com.google.common.collect.Lists;
import org.luvx.entity.User;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @ClassName: org.luvx.java8.stream
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/8/29 16:06
 * <p>
 * https://segmentfault.com/a/1190000019855034
 */
public class Demo {
    public static void main(String[] args) {
        method4();

        // List<User> list = Lists.newArrayList();
        // String s = list.stream().map(User::getUserName).collect(Collectors.joining(","));
        // System.out.println(s);
    }

    /**
     * 数组
     */
    public static void method0() {
        int[] array = {1, 2, 3, 4};
        Arrays.stream(array);
    }

    /**
     * 集合
     */
    public static void method1() {
        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(2);
        Stream<Integer> stream = integerList.stream();
        Stream<Integer> stream1 = integerList.parallelStream();
    }

    /**
     * Stream
     */
    public static void method2() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4);
        Stream.generate(Math::random).limit(5).forEach(System.out::println);
        Stream.iterate(0, i -> i + 1).limit(5).forEach(System.out::println);
    }

    // distinct
    // filter
    // sorted
    // limit
    // skip
    // map
    // flatMap
    // count

    public static void method3() {
        List<User> users = Lists.newArrayList();
        List<String> nameList = users.stream().map(User::getUserName).collect(Collectors.toList());

        Set<String> nameSet = users.stream().map(User::getUserName).collect(Collectors.toSet());

        Map<String, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getUserName, Function.identity(), (k1, k2) -> k2));
    }

    public static void method4() {
        List<String> items = Arrays.asList("apple", "apple", "banana",
                "apple", "orange", "banana", "papaya");
        Map<String, Long> result =
                items.stream().collect(
                        Collectors.groupingBy(
                                Function.identity(), Collectors.counting()
                        )
                );
        System.out.println(result);

        int[] arr = {3, 4, 5, 6, 3, 2, 3};

        // 数字出现的次数: {3:3,4:1,2:1,5:1,6:1}
        // List<Integer> list = new ArrayList(Arrays.asList(arr));
        /// Arrays.stream(arr).collect(Collectors.groupingBy());
        // Map<Integer, Long> s = list.stream().collect(
        //         Collectors.groupingBy(
        //                 t -> t.intValue(), Collectors.counting()
        //         )
        // );

        // 数字出现的下标: {2=[5], 3=[0, 4, 6], 4=[1], 5=[2], 6=[3]}
        Map<Integer, List<Integer>> collect = IntStream.range(0, arr.length)
                .boxed()
                .collect(Collectors.groupingBy(x -> arr[x], Collectors.toList()));
        System.out.println(collect);
    }
}
