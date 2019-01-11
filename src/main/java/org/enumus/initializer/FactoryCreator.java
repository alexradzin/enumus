package org.enumus.initializer;

import java.util.function.Function;

import static java.lang.String.format;

/**
 * Creates specific Factory
 */
public class FactoryCreator<T> implements Function<ValueData, Function<Object[], T>> {
    @Override
    public Function<Object[], T> apply(ValueData data) {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>)data.type();
        if (data.factory() == null) {
            return new ConstructorFactory<T>(clazz);
        }

        if (data.factory() == null && data.factoryMethod() != null) {
            return new FactoryMethodFactory<>(clazz, data.factoryMethod());
        }

        if (data.factory() != null && data.factoryMethod() != null) {
            return new FactoryFactory<T>(clazz, data.factory(), data.factoryArguments(), data.factoryMethod());
        }

        throw new IllegalStateException(format("ValueData %s cannot be interpreted", data));
    }

}
