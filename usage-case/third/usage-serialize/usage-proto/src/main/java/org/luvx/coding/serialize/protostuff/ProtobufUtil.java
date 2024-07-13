package org.luvx.coding.serialize.protostuff;

import com.alibaba.fastjson2.JSON;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

@Slf4j
public class ProtobufUtil {
    private static final JsonFormat.Printer printer = JsonFormat.printer();
    private static final JsonFormat.Parser  parser  = JsonFormat.parser();

    /**
     * proto -> json
     */
    public static String proto2Json(GeneratedMessage proto) {
        try {
            return printer.print(proto);
        } catch (InvalidProtocolBufferException e) {
            log.error("proto -> json异常", e);
            return null;
        }
    }

    /**
     * proto -> json -> javabean
     */
    public static <T> T proto2JavaBean(GeneratedMessage proto, Class<T> clazz) {
        String json = proto2Json(proto);
        return JSON.parseObject(json, clazz);
    }

    /**
     * javabean -> json -> proto
     */
    public static <T extends GeneratedMessage> T copyJavaBeanToProtoBean(Object o, T.Builder protoBuilder) {
        String json = JSON.toJSONString(o);
        try {
            parser.merge(json, protoBuilder);
            return (T) protoBuilder.build();
        } catch (InvalidProtocolBufferException e) {
            log.error("javabean -> json -> proto异常", e);
        }
        return null;
    }

    /**
     * javabean -> json -> proto -> byte[]
     */
    public static byte[] serializeFromJavaBean(Object o, GeneratedMessage.Builder protoBuilder) {
        GeneratedMessage m = copyJavaBeanToProtoBean(o, protoBuilder);
        return null != m ? m.toByteArray() : ArrayUtils.EMPTY_BYTE_ARRAY;
    }

    /**
     * byte[] -> proto -> javabean
     */
    public static <T> T deserializeToJavaBean(byte[] source, Parser<GeneratedMessage> parser, Class<T> clazz) {
        try {
            GeneratedMessage msg = parser.parseFrom(source);
            return proto2JavaBean(msg, clazz);
        } catch (InvalidProtocolBufferException e) {
            log.error("发序列化错误", e);
        }
        return null;
    }
}