package org.luvx.druid.lineage.utils;

import com.alibaba.druid.util.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author Ren, Xie
 */
public class EmptyUtils {
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static <T> boolean isNotEmpty(T[] array) {
        return !isEmpty(array);
    }

    public static <T> boolean isEmpty(Collection<T> coll) {
        return coll == null || coll.isEmpty();
    }

    public static <T> boolean isNotEmpty(Collection<T> coll) {
        return !isEmpty(coll);
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
        return !isEmpty(map);
    }

    public static <T> boolean isEmpty(T t) {
        return t == null || StringUtils.isEmpty(t.toString());
    }

    public static <T> boolean isNotEmpty(T t) {
        return !isEmpty(t);
    }
}
