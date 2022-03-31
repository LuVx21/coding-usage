package org.luvx.collections.list;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.luvx.entity.User;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListCase {
    public static void main(String[] args) {
        ArrayList<User> users = Lists.newArrayList();
        Map<Long, String> collect = users.stream().collect(Collectors.toMap(
                User::getId, User::getUserName, (a, b) -> b
        ));


        // m2();
        // m4();
        System.out.println(LocalDateTime.now());
    }

    public static void m4() {
        ArrayList<Integer> list = Lists.newArrayList(1, 2, 3);
        list.remove(Integer.valueOf(1));
    }

    public static void m3() {
        List<String> list = List.of();
        IntStream.range(0, list.size());

    }

    public static void m2() {
        Map<Integer, String> map = Map.of(1, "a", 2, "b", 3, "a");
        Map<String, List<Integer>> collect = List.of(1, 2, 3, 4).stream()
                .filter(map.keySet()::contains)
                .collect(groupingBy(map::get, toList()));
        System.out.println(collect);
    }

    public static void m1() {
        // List<Integer> list = List.of(1, 2, 3, 4, 5);
        // Spliterator<Integer> iterator = list.spliterator();
        //
        // int cnt = 0;
        // while (iterator.tryAdvance(i -> API.println(i + 1))) {
        //     cnt++;
        // }

        // Spliterator<Integer> sub;
        // while (iterator.estimateSize() > 2 &&
        //         (sub = iterator.trySplit()) != null) {
        // }
        // iterator.forEachRemaining(API::println);

        // ArrayList<User> users = Lists.newArrayList();
        // Map<Integer, List<Long>> collect = users.stream()
        //         .collect(Collectors.groupingBy(User::getAge, mapping(User::getId, Collectors.toList())));

    }
}
