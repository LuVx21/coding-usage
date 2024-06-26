package org_.eclipse.collections.impl.list.mutable.primitive;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.collections.api.list.primitive.ImmutableIntList;
import org.eclipse.collections.impl.list.immutable.primitive.ImmutableIntListFactoryImpl;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;
import org.junit.jupiter.api.Test;

@Slf4j
class Main {
    @Test
    void m1() {
    }

    @Test
    void m2() {
        IntArrayList list = new IntArrayList();
        list.add(10);
        System.out.println(list);

        ImmutableIntList with = ImmutableIntListFactoryImpl.INSTANCE.with(1, 2, 3, 4);
        System.out.println(with);
    }

    @Test
    void m3() {
    }

    @Test
    void m4() {
    }

    @Test
    void m5() {
    }
}
