package org.luvx.stream;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "Convert2MethodRef"})
public sealed interface Either<A, B> permits Left, Right {

    static <R> Either<Throwable, R> catching(UncheckedSupplier<R> block) {
        try {
            return right(block.get());
        } catch (Throwable throwable) {
            return left(throwable);
        }
    }

    static <L, R> Either<L, R> left(L left) {
        return new Left<>(left);
    }

    static <L, R> Either<L, R> right(R right) {
        return new Right<>(right);
    }

    default boolean isLeft() {
        return this instanceof Left<A, B>;
    }

    default boolean isRight() {
        return this instanceof Right<A, B>;
    }

    // default Either<A, B> onLeft(Consumer<A> action) {
    //     if (this instanceof Left<A, B>(A value)) {
    //         action.accept(value);
    //     }
    //     return this;
    // }
    //
    // default Either<A, B> onRight(Consumer<B> action) {
    //     if (this instanceof Right<A, B>(B value)) {
    //         action.accept(value);
    //     }
    //     return this;
    // }

    // default <C> C fold(Function<A, C> ifLeft, Function<B, C> ifRight) {
    //     return switch (this) {
    //         case Left<A, B>(A value) -> ifLeft.apply(value);
    //         case Right<A, B>(B value) -> ifRight.apply(value);
    //     };
    // }

    // default <C> Either<A, C> map(Function<B, C> f) {
    //     return flatMap(b -> right(f.apply(b)));
    // }

    // default <C> Either<C, B> mapLeft(Function<A, C> f) {
    //     return fold(a -> left(f.apply(a)), b -> right(b));
    // }

    // default <C> Either<A, C> flatMap(Function<B, Either<A, C>> f) {
    //     return switch (this) {
    //         case Left<A, B> ignored -> (Either<A, C>) ignored;
    //         case Right<A, B>(B right) -> f.apply(right);
    //     };
    // }

    // default Either<B, A> swap() {
    //     return fold(a -> right(a), b -> left(b));
    // }
    //
    // default B getOrElse(Function<A, B> _default) {
    //     return fold(_default, b -> b);
    // }

    // default B getOrNull() {
    //     return getOrElse(a -> null);
    // }

    // default Optional<B> toOptional() {
    //     return Optional.ofNullable(getOrNull());
    // }

    interface UncheckedSupplier<T> {
        T get() throws Throwable;
    }
}