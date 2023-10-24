package org.luvx.concurrent.vt;

import lombok.extern.slf4j.Slf4j;
import org.luvx.concurrent.entity.RunnableCase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        RunnableCase r = new RunnableCase(3);

        // 方式1
        Thread.ofPlatform().name("thread1").start(r);
        // 方式2
        Thread.startVirtualThread(r);

        // 方式4
        Thread.ofVirtual()
                .name("thread4")
                .uncaughtExceptionHandler((t, e) -> System.out.println(t.getName() + e.getMessage()))
                .unstarted(r)
                .start();

        // 方式5
        Thread.ofVirtual()
                .name("thread5")
                .factory()
                .newThread(r)
                .start();

        // 方式5
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        Future<?> submit = executorService.submit(r);
        Object o = submit.get();

        TimeUnit.SECONDS.sleep(3);
    }
}
