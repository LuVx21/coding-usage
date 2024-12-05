package org.luvx.coding.jdk._21;

import org.luvx.coding.jdk.concurrent.entity.Task;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StructuredTaskCase {
    private final static ScopedValue<String> context = ScopedValue.newInstance();

    public static void main(String[] args) throws Exception {
        // m1();
        m2();
        // m3();
    }

    /**
     * 结构化编程
     */
    public static void m1() throws IOException {
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
            Subtask<String> res1 = scope.fork(() -> Task.execute(-1));
            Subtask<String> res2 = scope.fork(() -> Task.execute(-1));
            Subtask<String> res3 = scope.fork(() -> Task.execute(-1));
            scope.join();

            System.out.println("结果:" + scope.result());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void m2() {
        ScopedValue.runWhere(context, "foobar", () -> {
            try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                scope.fork(() -> {
                    System.out.println("hello:" + context.get());
                    return "hello";
                });
                scope.fork(() -> {
                    System.out.println("say:" + context.get());
                    return 12;
                });
                try {
                    scope.join().throwIfFailed();
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void m3() throws IOException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<String> res1 = scope.fork(() -> {
                if (new Random().nextLong(3) == 0) {
                    throw new InterruptedException();
                }
                return Task.execute(-1);
            });
            Subtask<String> res2 = scope.fork(() -> Task.execute(-1));
            Subtask<String> res3 = scope.fork(() -> Task.execute(-1));
            scope.join();
            scope.throwIfFailed(Exception::new);

            // 如果所有任务均正常结束
            String result = Stream.of(res1, res2, res3)
                    .map(Subtask::get)
                    .collect(Collectors.joining(","));
            System.out.println("结果:" + result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
