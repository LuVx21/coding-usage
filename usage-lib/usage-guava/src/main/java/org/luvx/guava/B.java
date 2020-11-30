package org.luvx.guava;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class B {

    public static void main(String[] args) {
        final Joiner joiner = Joiner.on(",").skipNulls();
        final Splitter splitter = Splitter.on(",").trimResults().omitEmptyStrings();
        final CharMatcher digitMatcher = CharMatcher.anyOf("1234567890");

        String join = joiner.join(Lists.newArrayList("a", null, "b"));
        log.info("join:{}", join);

        for (String s : splitter.split(" a,  ,b,,")) {
            log.info("|{}|", s);
        }

        log.info("remove:{}", digitMatcher.removeFrom("1q2w3e4r5t"));
        log.info("retain:{}", digitMatcher.retainFrom("1q2w3e4r5t"));
        log.info("any:{}",
                CharMatcher.inRange('a', 'f')
                        .or(CharMatcher.is('n'))
                        .replaceFrom("abczn", "*")
        );
    }
}
