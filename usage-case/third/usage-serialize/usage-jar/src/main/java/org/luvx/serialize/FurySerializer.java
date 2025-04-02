package org.luvx.serialize;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.fury.Fury;
import org.apache.fury.config.Language;

import java.util.Set;

@Slf4j
public class FurySerializer implements Serializer {
    public static final  Fury          fury = Fury.builder().withLanguage(Language.JAVA).build();
    private static final Set<Class<?>> set  = Sets.newConcurrentHashSet();

    public FurySerializer() {
        set.addAll(fury.getClassResolver().getRegisteredClasses());
    }

    @Override
    public <T> byte[] serialize(T t) {
        register(t.getClass());
        return fury.serialize(t);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        register(clazz);
        return fury.deserializeJavaObject(bytes, clazz);
    }

    public static void register(Class<?> cls) {
        if (set.contains(cls)) {
            return;
        }
        fury.register(cls);
        set.add(cls);
    }
}