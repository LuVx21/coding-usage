package org.luvx.coding.serialize.protostuff;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.luvx.coding.serialize.protobuf.User;

import static org.luvx.coding.common.more.MorePrints.println;

class ProtobufUtilTest {

    @Test
    @SneakyThrows
    void m1() {
        User u = User.newBuilder()
                .setId("100").setName("foo").setSex("男")
                .build();
        String s = ProtobufUtil.proto2Json(u);

        byte[] bytes = u.toByteArray();
        println("json长度", s.length(), "字节数组长度", bytes.length);

        u = User.parseFrom(bytes);
        System.out.println(u);
    }
}