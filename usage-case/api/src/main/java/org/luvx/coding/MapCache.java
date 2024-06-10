package org.luvx.coding;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * map缓存实现
 */
public class MapCache<T> {

    /**
     * 默认存储1024个缓存
     */
    private static final int DEFAULT_CACHES = 1024;

    /**
     * 缓存容器
     */
    private final Map<String, CacheObject<T>> cachePool;

    public MapCache() {
        this(DEFAULT_CACHES);
    }

    public MapCache(int cacheCount) {
        cachePool = new ConcurrentHashMap<>(cacheCount);
    }

    /**
     * 读取一个缓存
     *
     * @param key 缓存key
     */
    public T get(String key) {
        CacheObject<T> cacheObject = cachePool.get(key);
        if (null != cacheObject) {
            long cur = System.currentTimeMillis() / 1000;
            if (cacheObject.getExpired() <= 0 || cacheObject.getExpired() > cur) {
                return cacheObject.getValue();
            }
        }
        return null;
    }

    /**
     * 读取一个hash类型缓存
     *
     * @param key   缓存key
     * @param field 缓存field
     */
    public T hget(String key, String field) {
        key = STR."\{key}:\{field}";
        return this.get(key);
    }

    /**
     * 设置一个缓存
     *
     * @param key   缓存key
     * @param value 缓存value
     */
    public void set(String key, T value) {
        this.set(key, value, -1);
    }

    /**
     * 设置一个缓存并带过期时间
     *
     * @param key     缓存key
     * @param value   缓存value
     * @param expired 过期时间，单位为秒
     */
    public void set(String key, T value, long expired) {
        expired = expired > 0 ? System.currentTimeMillis() / 1000 + expired : expired;
        CacheObject<T> cacheObject = new CacheObject<>(key, value, expired);
        cachePool.put(key, cacheObject);
    }

    /**
     * 设置一个hash缓存
     *
     * @param key   缓存key
     * @param field 缓存field
     * @param value 缓存value
     */
    public void hset(String key, String field, T value) {
        this.hset(key, field, value, -1);
    }

    /**
     * 设置一个hash缓存并带过期时间
     *
     * @param key     缓存key
     * @param field   缓存field
     * @param value   缓存value
     * @param expired 过期时间，单位为秒
     */
    public void hset(String key, String field, T value, long expired) {
        key = STR."\{key}:\{field}";
        expired = expired > 0 ? System.currentTimeMillis() / 1000 + expired : expired;
        CacheObject<T> cacheObject = new CacheObject<>(key, value, expired);
        cachePool.put(key, cacheObject);
    }

    /**
     * 根据key删除缓存
     *
     * @param key 缓存key
     */
    public void del(String key) {
        cachePool.remove(key);
    }

    /**
     * 根据key和field删除缓存
     *
     * @param key   缓存key
     * @param field 缓存field
     */
    public void hdel(String key, String field) {
        key = STR."\{key}:\{field}";
        this.del(key);
    }

    /**
     * 清空缓存
     */
    public void clean() {
        cachePool.clear();
    }

    @Getter
    @AllArgsConstructor
    static class CacheObject<T> {
        private String key;
        private T      value;
        private long   expired;
    }
}
