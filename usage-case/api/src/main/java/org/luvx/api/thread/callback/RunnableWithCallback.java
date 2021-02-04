package org.luvx.api.thread.callback;

import lombok.Builder;
import lombok.Setter;
import org.luvx.api.thread.entity.Task;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 带有回调的Runnable
 * 回调一般是用于异步处理的场景
 * <p>
 * 此处回调可以使代码逻辑清晰
 * 或者用于需要返回结果但又得使用Runnable的场景
 * 实现异步非阻塞
 * <p>
 * 获取子线程的执行结果:
 * 1. join子线程, 会阻塞主线程
 * 2. FutureTask 获取结果也是阻塞的
 * 3. 使用此类, 线程的执行结果继续处理(局限性也很明显)
 * 4. CompletableFuture
 * 5. Guava ListenableFuture FutureCallback
 * 其中1 2是阻塞式获取异步
 * 3介于中间
 * 4 5非阻塞获取异步线程
 *
 * @author Ren, Xie
 */
@Setter
@Builder
public class RunnableWithCallback<P, T> implements Runnable {
    P              args;
    Function<P, T> function;
    Consumer<T>    consumer;

    private RunnableWithCallback(P args, Function<P, T> function, Consumer<T> consumer) {
        this.args = args;
        this.function = function;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        T t = null;
        if (function != null) {
            t = function.apply(args);
        }
        if (consumer != null) {
            consumer.accept(t);
        }
    }

    public static void main(String[] args) {
        RunnableWithCallbackBuilder<String, String> builder = RunnableWithCallback.builder();
        RunnableWithCallback<String, String> r = builder
                .args("hello ")
                .function(s -> {
                    Task.execute(s);
                    return s + "world";
                })
                .consumer(s -> {
                    System.out.println("回调执行结束:" + s.toUpperCase());
                })
                .build();

        Thread thread = new Thread(r);
        thread.start();

        System.out.println("主线程结束......");
    }
}
