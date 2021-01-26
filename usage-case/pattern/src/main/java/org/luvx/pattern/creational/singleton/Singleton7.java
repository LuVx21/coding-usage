package org.luvx.pattern.creational.singleton;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 不使用锁实现单例模式
 * 无锁思想:CAS
 */
public class Singleton7 {
    private static final AtomicReference<Singleton7> INSTANCE = new AtomicReference<>();

    private Singleton7() {
    }

    public static final Singleton7 getInstance() {
        while (true) {
            Singleton7 instance = INSTANCE.get();
            if (instance != null) {
                return instance;
            }

            instance = new Singleton7();
            if (INSTANCE.compareAndSet(null, instance)) {
                return instance;
            }
        }
    }
}
