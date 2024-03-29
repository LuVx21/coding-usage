package org.luvx.coding.jdk.concurrent.threadpool;

import org.luvx.coding.jdk.concurrent.entity.CallableCase;
import org.luvx.coding.jdk.concurrent.utils.ThreadUtils;

import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 * ThreadPoolExecutor
 * <p>
 * execute()方法用于提交不需要返回值的任务,无法判断任务是否被线程池成功执行
 * submit()方法用于提交需要返回值的任务
 */
public class ThreadPoolCase {

    public static void main(String[] args) throws Exception {

        ThreadPoolExecutor executor = ThreadUtils.getThreadPool();

        for (int i = 0; i < 5; i++) {
            int index = i;
            // submit()方法会屏蔽所有错误信息
            // service.submit(() -> Task.execute("submit"));

            // submit替代方案1
            // service.execute(() -> Task.execute("execute"));

            // submit替代方案2
            // Future future = service.submit(() -> Task.execute("execute"));
            // future.get();

            // service.execute(new RunnableCase("1234" + index));

            FutureTask<String> task = new FutureTask<>(new CallableCase("1234" + i, -1));
            executor.execute(task);
        }

        long num = executor.getActiveCount();
        System.out.println(num);

        executor.shutdown();

        while (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            System.out.println("线程未结束...");
        }


        System.out.println("线程结束...");
    }
}
