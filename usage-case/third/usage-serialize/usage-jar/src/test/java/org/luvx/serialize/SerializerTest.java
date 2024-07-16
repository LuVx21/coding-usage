package org.luvx.serialize;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.luvx.coding.common.more.MorePrints;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Slf4j
class SerializerTest {
    Po po = new Po(1, "foo", new int[]{1, 2, 3, 4}, new InnerPo(1, "InnerPo1"),
            // List.of("a", "b")
            Lists.newArrayList(List.of("a", "b")),
            // Map.of("a","b")
            Maps.newHashMap(Map.of("a", "b"))
    );

    /**
     * 不可变list支持有问题
     */
    @Test
    void hessianTest() {
        var hessian = new HessianSerializer();
        byte[] bytes = hessian.serialize(po);

        var po1 = hessian.deserialize(bytes, Po.class);
        MorePrints.print(bytes.length, po1);
    }

    @Test
    void kryoTest() {
        var kryo = new KryoSerializer();
        byte[] bytes = kryo.serialize(po);

        var po1 = kryo.deserialize(bytes, Po.class);
        MorePrints.print(bytes.length, po1);

        bytes = kryo.serializeWithType(po);
        var o = kryo.deserialize(bytes);
        MorePrints.print(bytes.length, o);

        String s = kryo.serializeWithTypeToString(po);
        o = kryo.deserializeFromString(s);
        MorePrints.print(s.length(), o);
    }

    @Test
    void protostuffTest() {
        var protostuff = new ProtostuffSerializer();
        byte[] bytes = protostuff.serialize(po);

        var po1 = protostuff.deserialize(bytes, Po.class);
        MorePrints.print(bytes.length, po1);
    }

    @ToString
    @AllArgsConstructor
    static class Po implements Serializable {
        private int                 id;
        private String              name;
        private int[]               array;
        private InnerPo             innerPo;
        private List<String>        more;
        private Map<String, String> map;
    }

    @ToString
    @AllArgsConstructor
    static class InnerPo implements Serializable {
        private Integer id;
        private String  name;
    }
}