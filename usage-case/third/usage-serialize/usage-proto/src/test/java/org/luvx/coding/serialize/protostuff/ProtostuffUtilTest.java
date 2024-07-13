package org.luvx.coding.serialize.protostuff;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.util.List;

class ProtostuffUtilTest {
    @Test
    void m1() {
        Po po = new Po(1, "foo", new int[]{1, 2, 3, 4}, new InnerPo(1, "InnerPo1"), List.of("a", "b"));
        byte[] bytes = ProtostuffUtil.serialize(po);
        System.out.println("字节数组:" + bytes.length);

        Po newPo = ProtostuffUtil.deserialize(bytes, Po.class);
        System.out.println(newPo);
    }

    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Po {
        private Integer      id;
        private String       name;
        private int[]        array;
        private InnerPo      innerPo;
        private List<String> more;
    }

    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    static
    class InnerPo {
        private Integer id;
        private String  name;
    }
}