package org.luvx.coding.jdk.lang;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

import io.vavr.API;
import org.luvx.coding.common.more.MorePrints;

public class RefCase {
    public static void main(String[] args) {
        final ReferenceQueue<Object> queue = new ReferenceQueue<>();
        // 对象被GC时, 移入queue
        WeakReference<String> ref = new WeakReference<>("a", queue);
        // a 入队列 null
        MorePrints.println(ref.get(), ref.enqueue(), ref.get());
    }
}
