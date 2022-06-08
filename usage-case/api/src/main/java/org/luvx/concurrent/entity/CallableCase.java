package org.luvx.concurrent.entity;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

@AllArgsConstructor
public class CallableCase implements Callable<String> {
    private String name;

    @Override
    public String call() {
        return name + "-" + LocalDateTime.now();
    }
}
