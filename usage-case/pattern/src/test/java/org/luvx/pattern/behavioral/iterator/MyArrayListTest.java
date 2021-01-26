package org.luvx.pattern.behavioral.iterator;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class MyArrayListTest {
    @Test
    public void test1() {
        MyArrayList<Object> list = new MyArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.remove(1);

        list.add("bbb");
        MyIterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            log.info("元素:{}", next);
            if ("bbb".equals(next)) {
                iterator.remove();
            }
        }
    }
}