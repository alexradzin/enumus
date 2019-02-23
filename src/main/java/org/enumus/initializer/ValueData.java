package org.enumus.initializer;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    <T> T createInstance(Class<?> type) {
        try {
            return createInstanceStrategy(type);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked") // too many castings...
    private <T> T createInstanceStrategy(Class<?> fieldType) throws ReflectiveOperationException {
        Class<?> creator = factory == null ? type : factory;
        if (!"".equals(factoryMethod)) {
            Object f = null;
            Method m = findMethod(creator, factoryMethod, Arrays.stream(factoryMethodArguments).map(Object::getClass).toArray(Class[]::new));
            if (!Modifier.isStatic(m.getModifiers()) || factoryArguments.length > 0) {
                Constructor constructor = creator.getConstructor(Arrays.stream(factoryArguments).map(Object::getClass).toArray(Class[]::new));
                f = constructor.newInstance(factoryArguments);
            }
            return (T)m.invoke(f, factoryMethodArguments);
        }


        if (value.getClass().isArray() && fieldType.isArray() && isAssignable(type, value.getClass().getComponentType())) {
            return (T)value;
        }
        if (value.getClass().isArray() && Array.getLength(value) == 1 && isAssignable(type, Array.get(value, 0).getClass())) {
            return (T)Array.get(value, 0);
        }

        if (!value.getClass().isArray() && isAssignable(type, value.getClass())) {
            return (T)value;
        }

        Constructor constructor = creator.getConstructor(Arrays.stream(factoryArguments).map(Object::getClass).toArray(Class[]::new));
        return (T)constructor.newInstance(factoryArguments);
    }


    private static Method findMethod(Class<?> clazz, String name, Class<?>[] paramTypes) {
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



    private static final Map<Class, Class> primitiveWrapper = new HashMap<>();
    static {
        primitiveWrapper.put(byte.class, Byte.class);
        primitiveWrapper.put(short.class, Short.class);
        primitiveWrapper.put(int.class, Integer.class);
        primitiveWrapper.put(long.class, Long.class);
        primitiveWrapper.put(char.class, Character.class);
        primitiveWrapper.put(float.class, Float.class);
        primitiveWrapper.put(double.class, Double.class);
        primitiveWrapper.put(boolean.class, Boolean.class);
    }

    private static boolean isAssignable(Class<?> left, Class<?> right) {
        Class<?> l = left;
        Class<?> r = right;
        if ((left.isPrimitive() || right.isPrimitive()) && !(left.isPrimitive() && right.isPrimitive())) {
            l = primitiveWrapper.getOrDefault(left, left);
            r = primitiveWrapper.getOrDefault(right, right);
        }
        return l.isAssignableFrom(r);
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
}
