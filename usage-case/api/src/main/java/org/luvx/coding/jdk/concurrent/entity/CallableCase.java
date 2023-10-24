package org.luvx.coding.jdk.concurrent.entity;

import lombok.AllArgsConstructor;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class CallableCase implements Callable<String> {
    private String name;
    private int    second;

    @Override
    public String call() {
        return name + "-" + Task.execute(second);
    }
}
