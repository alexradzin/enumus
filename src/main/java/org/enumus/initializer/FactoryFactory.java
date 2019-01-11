package org.enumus.initializer;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class FactoryFactory<T> extends BaseFactory<T> {
    private final Class<T> clazz;
    private final Class<?> factoryClazz;
    private final Object[] factoryConstructorArgs;
    private final String factoryMethodName;

    public FactoryFactory(Class<T> clazz, Class<?> factoryClazz, Object[] factoryConstructorArgs, String factoryMethodName) {
        this.clazz = clazz;
        this.factoryClazz = factoryClazz;
        this.factoryConstructorArgs = factoryConstructorArgs;
        this.factoryMethodName = factoryMethodName;



    }

    @SuppressWarnings("unchecked")
    @Override
   public T apply(Object[] args) {

        System.out.println("1: " + Arrays.stream(factoryClazz.getDeclaredMethods()).collect(Collectors.toList()));
        System.out.println("2: " + Arrays.stream(factoryClazz.getDeclaredMethods()).filter(m -> m.getName().equals(factoryMethodName)).collect(Collectors.toList()));
        System.out.println("3: " + Arrays.stream(factoryClazz.getDeclaredMethods()).filter(m -> m.getName().equals(factoryMethodName)).filter(m -> isInvokable(m.getParameterTypes(), args)).collect(Collectors.toList()));
        System.out.println("4: " + Arrays.stream(factoryClazz.getDeclaredMethods()).filter(m -> m.getName().equals(factoryMethodName)).filter(m -> isInvokable(m.getParameterTypes(), args)).filter(m -> clazz.isAssignableFrom(m.getReturnType())).collect(Collectors.toList()));


        Method method = Arrays.stream(factoryClazz.getDeclaredMethods())
                .filter(m -> m.getName().equals(factoryMethodName))
                .filter(m -> isInvokable(m.getParameterTypes(), args))
                .filter(m -> Object.class.equals(m.getReturnType()) || clazz.isAssignableFrom(m.getReturnType())) // comparison with Object class is for generic methods. Is it OK? I doubt...
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        format("Cannot find factory method %s in class %s compatible with arguments %s",
                                factoryMethodName, factoryClazz,
                                Arrays.toString(args))));


        try {
            Object factory = Modifier.isStatic(method.getModifiers()) ?
                    null :
                    factoryConstructorArgs.length == 0 ?
                            factoryClazz.newInstance() :
                            factoryClazz.getConstructor(Arrays.stream(factoryConstructorArgs).map(Object::getClass).collect(Collectors.toList()).toArray(new Class[0])); // TODO: find applicable constructor
            return (T)method.invoke(factory, args);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

}
