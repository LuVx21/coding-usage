package org.luvx.stream;

public record Right<L, R>(R value) implements Either<L, R> {
    @Override
    public String toString() {
        return "right{" +
                "value=" + value +
                '}';
    }
}