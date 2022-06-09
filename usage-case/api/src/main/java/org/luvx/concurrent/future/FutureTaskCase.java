package org.luvx.concurrent.future;

import lombok.SneakyThrows;
import org.luvx.common.more.MorePrints;
import org.luvx.concurrent.entity.CallableCase;
import org.luvx.concurrent.entity.RunnableCase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.luvx.concurrent.utils.ThreadUtils.SERVICE;

public class FutureTaskCase {
    @SneakyThrows
    private void test1() {
        final RunnableCase runnable = new RunnableCase("a");
        String s = null;
        FutureTask<String> task = new FutureTask<>(runnable, s);
        SERVICE.execute(task);
        final String s1 = task.get();
        MorePrints.println(s, s1);
    }

    public static void future99() throws Exception {
        List<FutureTask<String>> list = new ArrayList<>(5);

        for (int i = 0; i < 6; i++) {
            FutureTask<String> task = new FutureTask<>(new CallableCase("1234" + i));
            SERVICE.execute(task);
            list.add(task);
        }

        SERVICE.shutdown();

        while (!SERVICE.awaitTermination(10, TimeUnit.SECONDS)) {
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
