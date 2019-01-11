package org.enumus.initializer;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import static java.lang.String.format;

public class FactoryMethodFactory<T> extends BaseFactory<T> {
    private  final Class<T> clazz;
    private final String factoryMethodName;

    public FactoryMethodFactory(Class<T> clazz, String method) {
        this.clazz = clazz;
        this.factoryMethodName = method;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T apply(Object[] args) {
        try {
            return (T) makeAccessible(Arrays.stream(clazz.getDeclaredMethods())
                    .filter(m -> Modifier.isStatic(m.getModifiers()))
                    .filter(m -> m.getName().equals(factoryMethodName))
                    .filter(m -> isInvokable(m.getParameterTypes(), args))
                    .filter(m -> clazz.isAssignableFrom(m.getReturnType()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            format("Cannot find factory method %s compatible with arguments %s",
                            factoryMethodName,
                            Arrays.toString(args)))))
                    .invoke(null, args);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
