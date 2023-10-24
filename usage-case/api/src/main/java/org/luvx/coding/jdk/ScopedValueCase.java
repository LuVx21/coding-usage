package org.luvx.coding.jdk;

public class ScopedValueCase {
    private final static ScopedValue<String> context = ScopedValue.newInstance();

    public static void main(String[] args) throws InterruptedException {
        ScopedValue
                .where(context, "foobar")
                // 仅在run内部有效
                .run(() -> {
                    System.out.println(context.get());
                });
        System.out.println(context.get());
    }
}
