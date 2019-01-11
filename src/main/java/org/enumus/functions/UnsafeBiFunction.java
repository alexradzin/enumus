package org.enumus.functions;

public interface UnsafeBiFunction<T, U, R, E extends Throwable> {
    R apply(T t, U u) throws E;
}
