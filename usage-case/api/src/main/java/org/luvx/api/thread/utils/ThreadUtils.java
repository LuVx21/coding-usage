package org.luvx.api.thread.utils;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @ClassName: org.luvx.api.thread.utils
 * @Description:
 * @Author: Ren, Xie
 * @Date: 2019/3/7 15:08
 */
public class ThreadUtils {

    private static final int size = 5;

    private static ExecutorService service;

    /**
     * 获取线程池
     *
     * @return
     */
    public static ExecutorService getThreadPool() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();

        ///
        // ThreadFactory factory = (Runnable r) -> {
        //     Thread thread = new Thread(r);
        //     thread.setName(r.getClass().getName());
        //     return thread;
        // };

        ExecutorService service = new ThreadPoolExecutor(size,
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
     *
     * @return
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
