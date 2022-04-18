package org.luvx.concurrent.future;

import static org.luvx.api.thread.utils.ThreadUtils.SERVICE;

import org.luvx.api.thread.entity.RunnableCase;
import org.luvx.api.thread.utils.ThreadUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

import io.vavr.API;
import lombok.SneakyThrows;

public class FutureTaskCase {
    @SneakyThrows
    private void test1() {
        final RunnableCase runnable = new RunnableCase("a");
        String s = null;
        FutureTask<String> task = new FutureTask<>(runnable, s);
        SERVICE.execute(task);
        final String s1 = task.get();
        API.println(s, s1);
    }

    public static void main(String[] args) {
        FutureTaskCase exec = new FutureTaskCase();
        exec.test1();
    }
}
