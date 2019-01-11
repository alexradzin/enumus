package org.enumus.initializer;

import jdk.internal.dynalink.NoSuchDynamicMethodException;

import javax.xml.crypto.NoSuchMechanismException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

class ValueData {
    private final String name;
    private final Object value;
    private final Class<?> type;
    private final Class<?> factory;
    private final String factoryMethod;
    private final Object[] factoryArguments;
    private final Object[] factoryMethodArguments;

    ValueData(String name, Object value, Class type, Class factory, String factoryMethod, Object[] factoryArguments, Object[] factoryMethodArguments) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.factory = factory;
        this.factoryMethod = factoryMethod;
        this.factoryArguments = factoryArguments;
        this.factoryMethodArguments = factoryMethodArguments;
    }

    String name() {
        return name;
    }

    Object value() {
        return value;
    }

    Class<?> type() {
        return type;
    }

    Class<?> factory() {
        return factory;
    }

    String factoryMethod() {
        return factoryMethod;
    }

    Object[] factoryArguments() {
        return factoryArguments;
    }

    Object[] factoryMethodArguments() {
        return factoryMethodArguments;
    }

    public <T> T createInstance() {
        try {
            return createInstanceStrategy();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private <T> T createInstanceStrategy() throws ReflectiveOperationException {
        Class<?> creator = factory == null ? type : factory;
        if (!"".equals(factoryMethod)) {
            Object f = null;
            Method m = findMethod(factory, factoryMethod, Arrays.stream(factoryMethodArguments).map(Object::getClass).collect(Collectors.toList()).toArray(new Class[0]));
            if (!Modifier.isStatic(m.getModifiers()) || factoryArguments.length > 0) {
                Constructor constructor = creator.getConstructor(Arrays.stream(factoryArguments).map(Object::getClass).collect(Collectors.toList()).toArray(new Class[0]));
                f = constructor.newInstance(factoryArguments);
            }
            return (T)m.invoke(f, factoryMethodArguments);
        }


        if (value.getClass().isArray() && Array.getLength(value) == 1 && isAssignable(type, Array.get(value, 0).getClass())) {
            return (T)Array.get(value, 0);
        }
        if (!value.getClass().isArray() && isAssignable(type, value.getClass())) {
            return (T)value;
        }

        Constructor constructor = creator.getConstructor(Arrays.stream(factoryArguments).map(Object::getClass).collect(Collectors.toList()).toArray(new Class[0]));
        return (T)constructor.newInstance(factoryArguments);
    }


    private static Method findMethod(Class clazz, String name, Class[] paramTypes) {
        try {
            return clazz.getMethod(name, paramTypes);
        } catch (NoSuchMethodException e) {
            return Arrays.stream(clazz.getMethods())
                    .filter(m -> m.getName().equals(name))
                    .filter(m -> m.getParameterCount() == paramTypes.length)
                    .filter(m -> areAssignable(m.getParameterTypes(), paramTypes))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchMethodError(String.format("%s.%s(%s)", clazz, name, Arrays.toString(paramTypes)))); //TODO: is it good to throw error?
        }
    }



    private static final Map<Class, Class> primitiveWrapper = new HashMap<Class, Class>() {{
        put(byte.class, Byte.class);
        put(short.class, Short.class);
        put(int.class, Integer.class);
        put(long.class, Long.class);
        put(char.class, Character.class);
        put(float.class, Float.class);
        put(double.class, Double.class);
        put(boolean.class, Boolean.class);
    }};

    private static boolean isAssignable(Class left, Class right) {
        if ((left.isPrimitive() || right.isPrimitive()) && ! (left.isPrimitive() && right.isPrimitive())) {
            left = primitiveWrapper.getOrDefault(left, left);
            right = primitiveWrapper.getOrDefault(right, right);
        }
        return left.isAssignableFrom(right);
    }

    private static boolean areAssignable(Class[] lefts, Class[] rights) {
        if (lefts.length != rights.length) {
            return false;
        }
        int n = lefts.length;
        for (int i = 0; i < n; i++) {
            if (!isAssignable(lefts[i], rights[i])) {
                return false;
            }
        }
        return true;
    }


    ValueData withValue(Object value) {
        return new ValueData(name, value, type, factory, factoryMethod, new Object[] {value}, new Object[] {value});
    }




//    @SuppressWarnings("unchecked")
//    private <T> Execution<Constructor, T> findConstructor(Class clazz, Object[] args) {
//        try {
//            //TODO: find suitable constructor
//            Constructor constructor = clazz.getConstructor(Arrays.stream(args).map(Object::getClass).collect(Collectors.toList()).toArray(new Class[0]));
//            Execution.ConstructorExecutor<T> exec = new Execution.ConstructorExecutor<T>(constructor, args);
//            new Execution<Constructor, T>(constructor, args);
//        } catch (ReflectiveOperationException e) {
//            throw new IllegalArgumentException(e);
//        }
//    }
//
//    @SuppressWarnings({"StreamToLoop", "unchecked"})
//    private <T> Execution<Method, T> findMethod(Class clazz, String methodName, Object[] args) {
//        try {
//            //TODO: find suitable method
//            return new Execution<>(
//                    clazz.getMethod(methodName, Arrays.stream(args).map(Object::getClass).collect(Collectors.toList()).toArray(new Class[0])),
//                    args,
//                    new Execution.MethodExecutor<T>());
//        } catch (ReflectiveOperationException e) {
//            throw new IllegalArgumentException(e);
//        }
//    }
//
//    private <E extends Executable, A, R> R reflect(UnsafeBiFunction<E, A, R, ReflectiveOperationException> f, E executable, A args) {
//        try {
//            return (R)f.apply(executable, args);
//        } catch (ReflectiveOperationException e) {
//            throw new IllegalStateException(e);
//        }
//    }
//
//
//    private static class Execution<E extends Executable, T> {
//        static class ConstructorExecutor<R> implements Supplier<R> {
//            private final Constructor constructor;
//            private final Object[] args;
//
//            ConstructorExecutor(Constructor constructor, Object[] args) {
//                this.constructor = constructor;
//                this.args = args;
//            }
//
//            @SuppressWarnings("unchecked")
//            @Override
//            public R get() {
//                try {
//                    return (R)constructor.newInstance(args);
//                } catch (ReflectiveOperationException e) {
//                    throw new IllegalArgumentException(e);
//                }
//            }
//        }
//        static class MethodExecutor<R> implements BiFunction<Method, Object[], R> {
//            @SuppressWarnings("unchecked")
//            @Override
//            public R apply(Method method, Object[] args) {
//                try {
//                    return (R)method.invoke(args);
//                } catch (ReflectiveOperationException e) {
//                    throw new IllegalArgumentException(e);
//                }
//            }
//        }
//
//        private final E executable;
//        private final Object[] args;
//        //private final BiFunction<E, Object[], T> executor;
//
//        public Execution(E executable, Object[] args/*, BiFunction<E, Object[], T> executor*/) {
//            this.executable = executable;
//            this.args = args;
//            //this.executor = executor;
//        }
//
////        public T execute() {
////            return executor.apply(executable, args);
////        }
//
//        public boolean isStatic() {
//            return Modifier.isStatic(executable.getModifiers());
//        }
//    }
}
