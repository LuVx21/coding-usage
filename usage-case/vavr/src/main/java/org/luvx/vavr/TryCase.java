package org.luvx.vavr;

import io.vavr.control.Try;

public class TryCase {

    public static void main(String[] args) {
        Try<String> try1 = Try
                // .of(() -> "a");
                // .ofSupplier(() -> "a");
                .ofCallable(() -> "a");
        // try1.onSuccess()
    }
}
