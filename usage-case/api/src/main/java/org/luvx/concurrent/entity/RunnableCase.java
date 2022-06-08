package org.luvx.concurrent.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RunnableCase implements Runnable {
    private String name;

    @Override
    public void run() {
//        Task.execute(name);
    }
}
