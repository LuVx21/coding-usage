package org.luvx.other;

public sealed interface Returned<T> {
    Returned.Undefined UNDEFINED = new Undefined();

    record ReturnValue<T>(T value) implements Returned {
    }

    record Undefined() implements Returned {
    }
}