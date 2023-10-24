package org.luvx.concurrent.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ThreadCase extends Thread {
    private int second;

    @Override
    public void run() {
        Task.execute(second);
    }
}
