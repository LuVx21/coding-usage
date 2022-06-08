package org.luvx.concurrent.future;

import lombok.extern.slf4j.Slf4j;
import org.luvx.concurrent.entity.Task;
import org.luvx.concurrent.utils.ThreadUtils;
import org.luvx.common.util.PrintUtils;
import org.luvx.common.util.Runs;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Slf4j
public class FutureRunner {
    public static <T> List<T> runInTime(Supplier<T>... suppliers) {
        CompletableFuture<T>[] array = Arrays.stream(suppliers)
                .map(supplier -> CompletableFuture.supplyAsync(supplier, ThreadUtils.SERVICE))
                .toArray(CompletableFuture[]::new);
        List<T> objects;
        try {
            objects = CompletableFuture.allOf(array)
                    .thenApply(v -> Arrays.stream(array).map(CompletableFuture::join).collect(toList()))
                    .get(10L, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("超时", e);
            throw new RuntimeException("超时", e);
        }
        return objects;
    }

    public static void main(String[] args) {
        Runs.runWithTime(() -> {
            List<Object> objects = FutureRunner.runInTime(
                    () -> Task.execute(5),
                    () -> Task.execute(1),
                    () -> Task.execute(1)
            );
            PrintUtils.println(objects);
        });
    }
}