package org.luvx.coding.jdk.concurrent.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RunnableCase implements Runnable {
    private int second;

    @Override
    public void run() {
        Task.execute(second);
    }
}
