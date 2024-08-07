package org.luvx.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import org.apache.commons.codec.binary.Base64;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.function.Supplier;

public class KryoSerializer implements Serializer {
    private static final String         DEFAULT_ENCODING = "UTF-8";
    private static final Supplier<Kryo> kryoSupplier     = () -> {
        final Kryo kryo = new Kryo();
        // 支持对象循环引用（否则会栈溢出）
        // 默认值就是 true，添加此行的目的是为了提醒维护者，不要改变这个配置
        kryo.setReferences(true);
        // 不强制要求注册类（注册行为无法保证多个 JVM 内同一个类的注册编号相同；而且业务系统中大量的 Class 也难以一一注册）
        // 默认值就是 false，添加此行的目的是为了提醒维护者，不要改变这个配置
        kryo.setRegistrationRequired(false);

        // ((DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
        //         .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());

        // https://github.com/EsotericSoftware/kryo#object-creation
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    };

    /**
     * <blockquote><pre>
     * // 获取池中的Kryo对象
     * Kryo kryo = kryoPool.obtain();
     * try{
     * } finally {
     *     // 将kryo对象归还到池中
     *     kryoPool.free(kryo);
     * }
     * </pre></blockquote>
     */
    private static final Pool<Kryo> kryoPool = new Pool<>(true, false, 8) {
        protected Kryo create() {
            return kryoSupplier.get();
        }
    };

    private static final ThreadLocal<Kryo> KRYO_LOCAL = ThreadLocal.withInitial(kryoSupplier);

    /**
     * 获得当前线程的 Kryo 实例
     *
     * @return 当前线程的 Kryo 实例
     */
    public Kryo getInstance() {
        return KRYO_LOCAL.get();
    }

    //-----------------------------------------------
    //      只序列化/反序列化对象
    //      序列化的结果里，不包含类型的信息
    //-----------------------------------------------

    /**
     * 将对象序列化为字节数组
     *
     * @param obj 任意对象
     * @param <T> 对象的类型
     * @return 序列化后的字节数组
     */
    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Output output = new Output(byteArrayOutputStream);

        Kryo kryo = getInstance();
        kryo.writeObject(output, obj);
        output.flush();

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 将对象序列化为 String
     * 利用了 Base64 编码
     *
     * @param obj 任意对象
     * @param <T> 对象的类型
     * @return 序列化后的字符串
     */
    public <T> String serializeToString(T obj) {
        try {
            return new String(Base64.encodeBase64(serialize(obj)), DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 将字节数组反序列化为原对象
     *
     * @param byteArray writeToByteArray 方法序列化后的字节数组
     * @param clazz     原对象的 Class
     * @param <T>       原对象的类型
     * @return 原对象
     */
    public <T> T deserialize(byte[] byteArray, Class<T> clazz) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        Input input = new Input(byteArrayInputStream);

        Kryo kryo = getInstance();
        return kryo.readObject(input, clazz);
    }

    /**
     * 将 String 反序列化为原对象
     * 利用了 Base64 编码
     *
     * @param str   writeToString 方法序列化后的字符串
     * @param clazz 原对象的 Class
     * @param <T>   原对象的类型
     * @return 原对象
     */
    public <T> T deserializeFromString(String str, Class<T> clazz) {
        try {
            return deserialize(Base64.decodeBase64(str.getBytes(DEFAULT_ENCODING)), clazz);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    //-----------------------------------------------
    //      序列化/反序列化对象，及类型信息
    //      序列化的结果里，包含类型的信息
    //      反序列化时不再需要提供类型
    //-----------------------------------------------

    /**
     * 将对象【及类型】序列化为字节数组
     *
     * @param obj 任意对象
     * @param <T> 对象的类型
     * @return 序列化后的字节数组
     */
    @Override
    public <T> byte[] serializeWithType(T obj) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Output output = new Output(os);

        getInstance().writeClassAndObject(output, obj);
        output.flush();

        return os.toByteArray();
    }

    /**
     * 将对象【及类型】序列化为 String
     * 利用了 Base64 编码
     *
     * @param obj 任意对象
     * @param <T> 对象的类型
     * @return 序列化后的字符串
     */
    public <T> String serializeWithTypeToString(T obj) {
        try {
            return new String(Base64.encodeBase64(serializeWithType(obj)), DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 将字节数组反序列化为原对象
     *
     * @param byteArray writeToByteArray 方法序列化后的字节数组
     * @param <T>       原对象的类型
     * @return 原对象
     */
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] byteArray) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        Input input = new Input(byteArrayInputStream);

        Kryo kryo = getInstance();
        return (T) kryo.readClassAndObject(input);
    }

    /**
     * 将 String 反序列化为原对象
     * 利用了 Base64 编码
     *
     * @param str writeToString 方法序列化后的字符串
     * @param <T> 原对象的类型
     * @return 原对象
     */
    public <T> T deserializeFromString(String str) {
        try {
            return deserialize(Base64.decodeBase64(str.getBytes(DEFAULT_ENCODING)));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}