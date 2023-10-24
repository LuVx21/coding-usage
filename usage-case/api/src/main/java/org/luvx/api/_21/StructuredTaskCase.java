package org.luvx.api._21;

import org.luvx.concurrent.entity.Task;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StructuredTaskCase {

    public static void main(String[] args) throws Exception {
        m1();
        m2();
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

    public static void m2() throws IOException {
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
