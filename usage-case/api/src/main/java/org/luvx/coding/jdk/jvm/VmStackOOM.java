package org.luvx.jvm;

public class VmStackOOM {
    private void a() {
        while (true) {
        }
    }

    private void b() {
        while (true) {
            (new Thread(this::a)).start();
        }
    }


    /**
     * -Xss2M
     */
    public static void main(String[] args) {
        VmStackOOM exec = new VmStackOOM();
        exec.b();
    }
}
