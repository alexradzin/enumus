package org.enumus.initializer;

import java.util.Arrays;

public class ConstructorFactory<T> extends BaseFactory<T> {
    private  final Class<T> clazz;

    public ConstructorFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T apply(Object[] args) {
        try {
            return (T)makeAccessible(Arrays.stream(clazz.getDeclaredConstructors())
                    .filter(c -> isInvokable(c.getParameterTypes(), args))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Cannot find constructor compatible with arguments " + Arrays.toString(args))))
                    .newInstance(args);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
