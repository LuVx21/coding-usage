package org.luvx.guava;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class D {
    public static void main(String[] args) {
        Function<Object, String> f = Functions.toStringFunction();

        Preconditions.checkNotNull(args, "参数不可为空");

        List<String> ss = Lists.newArrayList("a", "b");
        String b = Iterables.find(ss, s -> Objects.equals(s, "b"));
        log.info("iterables:{}", b);

        Optional<String> of = Optional.empty();
    }
}

