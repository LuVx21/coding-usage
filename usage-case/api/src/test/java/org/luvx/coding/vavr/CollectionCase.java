package org.luvx.coding.vavr;

import org.apache.commons.lang3.tuple.MutablePair;

import io.vavr.Tuple2;
import io.vavr.collection.HashMultimap;
import io.vavr.collection.List;
import io.vavr.collection.Traversable;
import io.vavr.control.Option;

public class CollectionCase {
    public static void main(String[] args) {
        m1();
        // m2();
    }

    public static void m2() {
        HashMultimap<String, Integer> of = HashMultimap
                .withSeq()
                // .withSet()
                // .withSortedSet()
                .of("a", 1, "a", 1);
        Option<Traversable<Integer>> a = of.get("a");
        System.out.println(of);

        // java.util.stream.Stream.of().count();
        // io.vavr.collection.Stream.of().count(t -> true);
    }

    public static void m1() {
        int i = List.of(1, 2, 3).sum().intValue();
        List<Tuple2<String, Integer>> tuple2s = List.of("1", "2", "3").zipWithIndex();
        System.out.println(tuple2s);

        MutablePair<Integer, String> tuple1 = MutablePair.of(0, "a"), tuple2 = MutablePair.of(0, "b");
        java.util.List<MutablePair<Integer, String>> users = List.of(tuple1, tuple2)
                .zipWithIndex()
                .map(t -> {
                            t._1.left = t._2;
                            return t._1;
                        }
                )
                .toJavaList();
        System.out.println(users);
    }
}

