package org.luvx.api.thread.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ThreadCase extends Thread {
    private String name;

    @Override
    public void run() {
//        Task.execute(name);
    }
}
