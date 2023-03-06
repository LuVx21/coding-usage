package org.luvx.stream;

public record Left<L, R>(L value) implements Either<L, R> {
    @Override
    public String toString() {
        return "left{" +
                "value=" + value +
                '}';
    }
}