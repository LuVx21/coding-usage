package org.luvx.concurrent.future;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import org.jetbrains.annotations.NotNull;
import org.luvx.coding.common.more.MorePrints;
import org.luvx.concurrent.entity.CallableCase;
import org.luvx.concurrent.entity.Task;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static org.luvx.concurrent.utils.ThreadUtils.SERVICE;

/**
 * 获取子线程的执行结果
 *
 * @author Ren, Xie
 */
@Slf4j
public class FutureCase {
    /**
     * 阻塞主线程
     *
     * @throws Exception
     */
    public static void future0() throws Exception {
        AtomicReference<String> result = new AtomicReference<>();
        Thread thread = new Thread(() -> result.set(Task.execute(5)));
        thread.start();
        thread.join();

        log.info("处理线程执行结果:{}", result.get());
    }

    /**
     * 阻塞主线程
     *
     * @throws Exception
     */
    public static void future1() throws Exception {
        Future<String> future = SERVICE.submit(new CallableCase("1234", -1));
        String result = future.get();
        log.info("[{}] -> {}", Thread.currentThread().getName(), result);
        log.info("future outer end.....");

        log.info("处理线程执行结果:{}", result);
    }

    /**
     * 不阻塞主线程
     *
     * @throws Exception
     */
    @SneakyThrows
    public static void future2() {
        // CompletableFuture.runAsync(() -> {});
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() ->
                Task.execute(5), SERVICE
        ).thenApply(result -> {
            log.info("处理线程执行结果:{}", result);
            return result + "-" + LocalDateTime.now();
        }).exceptionally(e -> {
            log.warn("异步异常结束", e);
            return "";
        });
        // .thenCompose()
        // .thenCombine()
        MorePrints.println(future.get());
    }

    public static void guavaFuture() {
        ListeningExecutorService guavaExecutor = MoreExecutors.listeningDecorator(SERVICE);
        final ListenableFuture<String> listenableFuture = guavaExecutor.submit(new CallableCase("1234", -1));
        listenableFuture.addListener(() -> {
            try {
                String result = listenableFuture.get();
                log.info("Listener 处理线程执行结果:{}", result);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, SERVICE);

        Futures.addCallback(listenableFuture, new FutureCallback<>() {
            @Override
            public void onSuccess(String result) {
                log.info("Callback 处理线程执行结果:{}", result);
            }

            @Override
            public void onFailure(@NotNull Throwable e) {
                log.warn("Callback 异步异常结束", e);
            }
        }, SERVICE);

        log.info("guavaFuture执行结束");
    }

    public static void main(String[] args) throws Exception {
        // future0();
        // future1();
        // future2();
        // guavaFuture();

        log.info("main end.....");
    }
}