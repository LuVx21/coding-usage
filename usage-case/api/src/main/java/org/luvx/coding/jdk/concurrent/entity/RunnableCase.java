package org.luvx.concurrent.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RunnableCase implements Runnable {
    private int second;

    @Override
    public void run() {
        Task.execute(second);
    }
}
