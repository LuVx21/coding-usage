package com.google.common.eventbus;

public class Main {
    void main() {
        final EventBus eventBus = new EventBus();
        DataObserver1 observer1 = new DataObserver1();
        DataObserver2 observer2 = new DataObserver2();

        eventBus.register(observer1);
        eventBus.register(observer2);

        System.out.println("============   start  ====================");

        // 只有注册的参数类型为String的方法会被调用
        eventBus.post("post string method");
        eventBus.post(123);

        System.out.println("============ after unregister ============");
        // 注销observer2
        eventBus.unregister(observer2);
        eventBus.post("post string method");
        eventBus.post(123);

        System.out.println("============    end           =============");
    }

    static class DataObserver1 {
        @Subscribe
        public void func(String msg) {
            System.out.println("String msg: " + msg);
        }
    }

    static class DataObserver2 {
        @Subscribe
        public void func(Integer msg) {
            System.out.println("Integer msg: " + msg);
        }
    }
}
