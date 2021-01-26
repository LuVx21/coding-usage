package org.luvx.api.thread.notify;

public class ThreadLocalCase {
    private static ThreadLocal<String> local = new ThreadLocal<>();

    static class A implements Runnable {
        @Override
        public void run() {
            local.set("线程A");
            System.out.println(local.get());
        }
    }

    static class B implements Runnable {
        @Override
        public void run() {
            local.set("线程B");
            System.out.println(local.get());
        }
    }

    public static void main(String[] args) {
        local.set("线程main");
        new Thread(new A()).start();
        new Thread(new B()).start();
        System.out.println(local.get());
    }
}
