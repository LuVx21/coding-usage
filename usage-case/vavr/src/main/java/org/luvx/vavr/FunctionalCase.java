package org.luvx.vavr;


import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;

public class FunctionalCase {
    public static void main(String[] args) {
        Function2<Integer, Integer, Integer> f1 = Integer::sum;
        Function2<Integer, Integer, Integer> f2 = f1.andThen(v -> v * 10);
        Integer apply = f2.apply(1, 2);
        System.out.println(apply);

        Function1<String, String> f3 = String::toUpperCase;
        Function1<Object, String> f4 = f3.compose(Object::toString);
        String str = f4.apply(List.of("s", 2));
        System.out.println(str);
    }
}
