package org.luvx.api.thread.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RunnableCase implements Runnable {
    private String name;

    @Override
    public void run() {
//        Task.execute(name);
    }
}
