package org.luvx.coding.serialize.protostuff;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.luvx.coding.common.annotation.Immutable;

import jakarta.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ProtostuffUtil {
    private static final Map<Class<?>, Schema<?>> cache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    private static <T> Schema<T> getSchema(Class<T> clazz) {
        return (Schema<T>) cache.computeIfAbsent(clazz, RuntimeSchema::getSchema);
    }

    /**
     * 序列化对象
     */
    public static <T> byte[] serialize(T o) {
        if (o == null) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }

        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) getSchema(o.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        byte[] bytes;
        try {
            bytes = ProtostuffIOUtil.toByteArray(o, schema, buffer);
        } catch (Exception e) {
            throw new RuntimeException("序列化异常");
        } finally {
            buffer.clear();
        }
        return bytes;
    }

    /**
     * 反序列化对象
     */
    @Nullable
    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        if (ArrayUtils.isEmpty(bytes)) {
            return null;
        }

        Schema<T> schema = getSchema(clazz);
        T instance = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, instance, schema);
        return instance;
    }

    /**
     * 序列化列表
     */
    public static <T> byte[] serializeList(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }

        @SuppressWarnings("unchecked")
        Schema<T> schema = (Schema<T>) getSchema(list.getFirst().getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ProtostuffIOUtil.writeListTo(bos, list, schema, buffer);
            return bos.toByteArray();
        } catch (Exception e) {
            log.error("Failed to serializer, obj list:{}", list, e);
            throw new RuntimeException("Failed to serializer");
        } finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化列表
     */
    @Immutable
    public static <T> List<T> deserializeList(byte[] bytes, Class<T> clazz) {
        if (ArrayUtils.isEmpty(bytes)) {
            return Collections.emptyList();
        }

        Schema<T> schema = getSchema(clazz);
        try {
            return ProtostuffIOUtil.parseListFrom(new ByteArrayInputStream(bytes), schema);
        } catch (IOException e) {
            log.error("Failed to deserialize", e);
            throw new RuntimeException("Failed to deserialize");
        }
    }
}