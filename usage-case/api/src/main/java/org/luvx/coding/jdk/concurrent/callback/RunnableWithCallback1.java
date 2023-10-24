package org.luvx.coding.jdk.concurrent.callback;

/**
 * 带有回调的Runnable
 *
 * @author Ren, Xie
 */
public abstract class RunnableWithCallback1<P, T> implements Runnable {
    P args;

    public RunnableWithCallback1(P args) {
        this.args = args;
    }

    public abstract T run1(P args);

    public abstract void callback(T result);

    @Override
    public void run() {
        T result = run1(args);
        callback(result);
    }

    public static void main(String[] args) {
        RunnableWithCallback1<String, String> r = new RunnableWithCallback1<>("hello ") {
            @Override
            public String run1(String args) {
                return args + "world1";
            }

            @Override
            public void callback(String result) {
                System.out.println(result.toUpperCase());
            }
        };

        Thread thread = new Thread(r);
        thread.start();
    }
}
