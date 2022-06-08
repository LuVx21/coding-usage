package org.luvx.concurrent.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ThreadCase extends Thread {
    private String name;

    @Override
    public void run() {
//        Task.execute(name);
    }
}
