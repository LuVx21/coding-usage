package org.luvx.guava;

import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class A {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(3);
        ListeningExecutorService les = MoreExecutors.listeningDecorator(es);
        ListenableFuture<Integer> future = les.submit(
                () -> {
                    if (new Random().nextInt(3) == 2) {
                        throw new RuntimeException("等于2");
                    }
                    return 1;
                }
        );

        FutureCallback<Integer> futureCallback = new FutureCallback<Integer>() {
            @Override
            public void onSuccess(@Nullable Integer integer) {
                log.info("--{}", integer);
            }

            @Override
            public void onFailure(Throwable t) {
                log.error("--{}", t.getMessage());
            }
        };
        Futures.addCallback(future, futureCallback, es);
    }
}
