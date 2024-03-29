package org.luvx.coding.jdk.concurrent.callback.callback;

public class Callerimpl implements Caller {

    public void call(String question, Callback case1) {
        System.out.println("this is caller " + question);
        case1.fix("Test", this);
    }

    @Override
    public void called() {
        System.out.println("is called");
    }
}
