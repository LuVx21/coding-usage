package org.luvx.vavr;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

import io.vavr.API;
import io.vavr.control.Option;

public class OptionCase {
    public static void main(String[] args) {
    }

    private static void m2() {
        Option.of(null)
                .isEmpty();
    }

    private static void m1() {
        List<Integer> list = Lists.newArrayList(1, 2, 3);
        Optional<Integer> first = list.stream()
                .filter(i -> i > 3)
                .findFirst();
        Integer orElse = Option.ofOptional(first)
                .peek(API::println)
                .getOrElse(-1);
        System.out.println(orElse);
    }
}
