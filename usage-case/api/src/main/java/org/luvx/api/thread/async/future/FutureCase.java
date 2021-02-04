package org.luvx.api.thread.async.future;

import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.luvx.api.thread.entity.CallableCase;
import org.luvx.api.thread.entity.Task;
import org.luvx.api.thread.utils.ThreadUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 获取子线程的执行结果
 *
 * @author Ren, Xie
 */
@Slf4j
public class FutureCase {

    static ExecutorService service = ThreadUtils.getThreadPool();

    /**
     * 阻塞主线程
     *
     * @throws Exception
     */
    public static void future0() throws Exception {
        AtomicReference<String> result = new AtomicReference<>();
        Thread thread = new Thread(() -> result.set(Task.execute("1234") + "-" + LocalDateTime.now()));
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
        Future<String> future = service.submit(new CallableCase("1234"));
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
    public static void future2() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() ->
                Task.execute("1234") + "-" + LocalDateTime.now(), service
        );
        future.thenApply(result -> {
            log.info("处理线程执行结果:{}", result);
            return result + "-" + LocalDateTime.now();
        });
        future.exceptionally(e -> {
            log.warn("异步异常结束", e);
            return "";
        });
    }

    public static void guavaFuture() {
        ListeningExecutorService guavaExecutor = MoreExecutors.listeningDecorator(service);
        final ListenableFuture<String> listenableFuture = guavaExecutor.submit(new CallableCase("1234"));
        listenableFuture.addListener(() -> {
            try {
                String result = listenableFuture.get();
                log.info("Listener 处理线程执行结果:{}", result);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, service);

        Futures.addCallback(listenableFuture, new FutureCallback<>() {
            @Override
            public void onSuccess(String result) {
                log.info("Callback 处理线程执行结果:{}", result);
            }

            @Override
            public void onFailure(@NotNull Throwable e) {
                log.warn("Callback 异步异常结束", e);
            }
        }, service);

        log.info("guavaFuture执行结束");
    }

    public static void future99() throws Exception {
        List<FutureTask<String>> list = new ArrayList<>(5);

        for (int i = 0; i < 6; i++) {
            FutureTask<String> task = new FutureTask<>(new CallableCase("1234" + i));
            service.execute(task);
            list.add(task);
        }

        service.shutdown();

        while (!service.awaitTermination(10, TimeUnit.SECONDS)) {
            System.out.println("线程未结束...");
        }

        System.out.println("-------------------------------");

        for (FutureTask<String> task : list) {
            if (!task.isDone()) {
                System.out.println("继续等待......");
                continue;
            }
            String result = task.get();
            System.out.println(result);
        }
    }

    public static void main(String[] args) throws Exception {
        // future0();
        // future1();
        // future2();
        // guavaFuture();

        log.info("main end.....");
    }
}