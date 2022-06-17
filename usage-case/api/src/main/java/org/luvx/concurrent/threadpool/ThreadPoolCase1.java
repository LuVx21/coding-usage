package org.luvx.concurrent.threadpool;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import org.luvx.concurrent.entity.CallableCase;
import org.luvx.concurrent.utils.ThreadUtils;

import java.util.concurrent.ExecutionException;

public class ThreadPoolCase1 {

    private static ListeningExecutorService listeningExecutor = ThreadUtils.getThreadPool1();

    public void method() {
        final ListenableFuture<String> future = listeningExecutor.submit(new CallableCase("1234"));

        future.addListener(() -> {
            try {
                System.out.println("收到通知..." + future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }, listeningExecutor);
    }

    public static void main(String[] args) {
        ThreadPoolCase1 pollCase = new ThreadPoolCase1();
        pollCase.method();
    }
}