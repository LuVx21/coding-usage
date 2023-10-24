package org.luvx.coding.jdk.concurrent.future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.luvx.coding.common.more.MorePrints;
import org.luvx.coding.jdk.concurrent.entity.CallableCase;
import org.luvx.coding.jdk.concurrent.entity.RunnableCase;
import org.luvx.coding.jdk.concurrent.utils.ThreadUtils;

import lombok.SneakyThrows;

public class FutureTaskCase {
    @SneakyThrows
    private void test1() {
        final RunnableCase runnable = new RunnableCase(-1);
        String s = null;
        FutureTask<String> task = new FutureTask<>(runnable, s);
        ThreadUtils.SERVICE.execute(task);
        final String s1 = task.get();
        MorePrints.println(s, s1);
    }

    public static void future99() throws Exception {
        List<FutureTask<String>> list = new ArrayList<>(5);

        for (int i = 0; i < 6; i++) {
            FutureTask<String> task = new FutureTask<>(new CallableCase("1234" + i, -1));
            ThreadUtils.SERVICE.execute(task);
            list.add(task);
        }

        ThreadUtils.SERVICE.shutdown();

        while (!ThreadUtils.SERVICE.awaitTermination(10, TimeUnit.SECONDS)) {
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

    public static void main(String[] args) {
        FutureTaskCase exec = new FutureTaskCase();
        exec.test1();
    }
}
