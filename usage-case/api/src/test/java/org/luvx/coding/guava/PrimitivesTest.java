package org.luvx.coding.guava;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Primitives;
import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedInts;
import org.junit.jupiter.api.Test;
import org.luvx.coding.common.more.MorePrints;

import java.util.List;
import java.util.Set;

class PrimitivesTest {
    @Test
    void m1() {

        List<Integer> a = Ints.asList(1, 2, 3);
        List<Integer> b = List.of(1, 2, 3);
        MorePrints.println(a, b);

        Set<Class<?>> classes = Primitives.allPrimitiveTypes();
        System.out.println(classes);
        Set<Class<?>> classes1 = Primitives.allWrapperTypes();
        System.out.println(classes1);
        long aLong = UnsignedInts.toLong(10);

        UnsignedInteger unsignedInteger = UnsignedInteger.fromIntBits(10);
        MorePrints.println(aLong, unsignedInteger);
    }
}
