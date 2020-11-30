package org.luvx.guava;

import com.google.common.primitives.Ints;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class C {
    public static void main(String[] args) {
        int[] concat = Ints.concat(new int[]{1, 2}, new int[]{3, 4, 5});
        List<Integer> list = Ints.asList(concat);
        log.info("join:{}", Ints.join(",", Ints.toArray(list)));
        log.info("max:{}, min:{}", Ints.max(concat), Ints.min(concat));
        log.info("6:{}", Ints.contains(concat, 6));
    }
}
