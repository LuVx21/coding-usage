package org.luvx.serialize.protostuff;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

public class Main {
    static RuntimeSchema<Po> poSchema = RuntimeSchema.createFrom(Po.class);

    private static byte[] serialize(Po po) {
        return ProtostuffIOUtil.toByteArray(po, poSchema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }

    private static Po deserialize(byte[] bytes) {
        Po po = poSchema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, po, poSchema);
        return po;
    }

    public void main(String[] args) {
        Po po = new Po(1, "foo", new int[]{1, 2, 3, 4}, new InnerPo(1, "InnerPo1"), List.of("a", "b"));
        byte[] bytes = serialize(po);
        System.out.println("字节数组:" + bytes.length);

        Po newPo = deserialize(bytes);
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
