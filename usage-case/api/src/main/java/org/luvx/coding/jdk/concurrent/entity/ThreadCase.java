package org.luvx.coding.jdk.concurrent.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ThreadCase extends Thread {
    private int second;

    @Override
    public void run() {
        Task.execute(second);
    }
}
