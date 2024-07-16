package org.luvx.serialize;

public interface Serializer {
    /**
     * 序列化对象,结果不含有类型信息
     *
     * @param t   对象
     * @param <T> 返回序列化字节数组
     */
    <T> byte[] serialize(T t);

    /**
     * 反序列化字节数组为类对象, 字节数组中不含类型信息
     *
     * @param bytes 字节数组
     * @param clazz 待反序列化类
     * @param <T>   反序列化对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);

    // -------------------------------------------以下字节数组中含有类型信息-------------------------------------------

    /**
     * 序列化对象,结果含有类型信息
     */
    default <T> byte[] serializeWithType(T obj) {
        return serialize(obj);
    }

    /**
     * 字节数组中含有类型信息
     *
     * @param bytes 字节数组
     * @param <T>   反序列化对象
     */
    @SuppressWarnings("unchecked")
    default <T> T deserialize(byte[] bytes) {
        return (T) deserialize(bytes, Object.class);
    }
}
