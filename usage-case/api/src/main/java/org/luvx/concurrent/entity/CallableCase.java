package org.luvx.concurrent.entity;

import lombok.AllArgsConstructor;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class CallableCase implements Callable<String> {
    private int second;

    @Override
    public String call() {
        return Task.execute(second);
    }
}
