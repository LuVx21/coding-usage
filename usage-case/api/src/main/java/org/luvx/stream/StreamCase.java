package org.luvx.stream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.luvx.entity.User;

import com.google.common.collect.Lists;

import io.vavr.Tuple;
import io.vavr.Tuple1;
import lombok.extern.slf4j.Slf4j;

/**
 * @author renxie
 */
@Slf4j
public class StreamCase {
    public void streamCreate() throws Exception {
        String path = "~/1.txt";
        Stream<String> lines = Files.lines(Paths.get(path), Charset.defaultCharset());
        lines.forEach(System.out::println);
        Stream<Integer> stream = Stream.iterate(0, n -> n + 2).limit(5);
        stream.forEach(System.out::println);
        Stream<Double> stream1 = Stream.generate(Math::random).limit(5);
        stream1.forEach(System.out::println);
    }

    public void operate() {
        Stream.of(1, 1, 2, 3, 4, 5)
                .distinct()
                .limit(6)
                .skip(2)
                .map(i -> i * 2)
                .forEach(System.out::println);

        Stream.of("11211", "33233")
                .map(w -> w.split("2"))
                .flatMap(Arrays::stream)
                .forEach(System.out::println);
    }

    public void match() {
        log.info("{}",
                Stream.of(1, 2, 3, 4, 5)
                        // .allMatch(i -> i > 0)
                        .anyMatch(i -> i == 3)
                // .noneMatch(i -> i < 0)
        );
    }

    public void result() {
        Stream.of(1, 2, 3, 4, 5)
                //                .count()
                .collect(counting());
    }

    public void sort() {
        List<User> users = Lists.newArrayList(
                User.builder().userName("foo").build(),
                User.builder().userName("bar").build()
        );
        final int[] i = {1};
        List<User> sorted = users.stream()
                .sorted(Comparator.comparing(User::getUserName))
                .peek(u -> u.setId(i[0]++))
                .collect(toList());
        System.out.println(sorted);
    }


    public Map<Long, Integer> mapSort(Map<Long, Integer> map) {
        List<Entry<Long, Integer>> entries = Lists.newArrayList(map.entrySet());
        entries.sort(Comparator.comparing(Entry<Long, Integer>::getValue));
        List<Integer> priorityList = Lists.newArrayList(2, 3);
        return priorityList.stream()
                .filter(i -> i <= entries.size())
                .collect(toMap(no -> entries.get(no - 1).getKey(), no -> no));
    }

    /**
     * <pre>
     * List<User> -> Map<Integer, List<Integer>>
     * </pre>
     */
    public void groupingByCase(List<User> users) {
        Map<Integer, List<Long>> collect = users.stream()
                .collect(Collectors.groupingBy(User::getAge, mapping(User::getId, toList())));
    }

    /**
     * <pre>
     * List<User> -> Map<Boolean, List<Integer>>
     * </pre>
     */
    public void partitioningBy(List<User> users) {
        Map<Boolean, List<Long>> collect = users.stream()
                .collect(Collectors
                        .partitioningBy(u -> u.getAge() > 18, mapping(User::getId, toList())));
    }


    @SuppressWarnings("checkstyle:MagicNumber")
    public static void main(String[] args) throws Exception {
        // StreamCase exec = new StreamCase();
        // exec.operate();

        //        System.out.println(Integer.parseInt("400") == 400);
        List<User> list = List.of(User.builder().id(1).build(),
                User.builder().id(1).userName("foo").build());

        Map<Long, User> collect = list.stream().collect(toMap(User::getId, identity(), (a, b) -> b));
        System.out.println(collect);
    }

    /**
     * list 按照另一个 list 排序
     */
    public List<User> sortByList(List<Long> ids, List<User> users) {
        Map<Long, User> map = users.stream()
                .collect(toMap(User::getId, identity(), (a, b) -> b));
        return ids.stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    public void a(List<User> users) {
        Map<Integer, List<Tuple1<Long>>> collect = users.stream()
                .collect(Collectors.groupingBy(User::getAge,
                        mapping(u -> Tuple.of(u.getId()), toList()))
                );
    }

    public void b(List<User> users) {
        Map<Integer, Map<Long, Tuple1<Long>>> collect1 = users.stream()
                .collect(groupingBy(User::getAge, mapping(User::getId, toList())))
                .entrySet().stream()
                .collect(toMap(Entry::getKey,
                        e -> aa(e.getValue()).stream().collect(toMap(Tuple1::_1, identity())))
                );

        Map<Integer, Map<Long, Tuple1<Long>>> collect2 = users.stream()
                .collect(groupingBy(
                        User::getAge,
                        mapping(User::getId, toMap(identity(), Tuple::of, (o, n) -> n)))
                );
    }

    private List<Tuple1<Long>> aa(List<Long> list) {
        return List.of();
    }
}
