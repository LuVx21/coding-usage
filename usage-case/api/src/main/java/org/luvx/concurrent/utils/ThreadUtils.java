package org.luvx.concurrent.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * @ClassName: org.luvx.api.thread.utils
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/3/7 15:08
 */
public class ThreadUtils {
    public static ExecutorService SERVICE = ThreadUtils.getThreadPool();

    private static final int size = 5;
    private static ExecutorService service;

    /**
     * 获取线程池
     */
    public static ThreadPoolExecutor getThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();

        ///
        // ThreadFactory factory = (Runnable r) -> {
        //     Thread thread = new Thread(r);
        //     thread.setName(r.getClass().getName());
        //     return thread;
        // };

        ThreadPoolExecutor service = new ThreadPoolExecutor(size,
                size << 1,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(100),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
        ThreadUtils.service = service;
        return service;
    }

    /**
     * 获取Guava封装的线程池
     */
    public static ListeningExecutorService getThreadPool1() {
        // ExecutorService service = getThreadPool();
        if (service == null) {
            service = getThreadPool();
        }
        ListeningExecutorService service1 = MoreExecutors.listeningDecorator(service);
        return service1;
    }
}
