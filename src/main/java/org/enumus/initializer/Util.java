package org.enumus.initializer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

class Util {
    static String name(Object self) {
        try {
            return (String) self.getClass().getMethod("name").invoke(self);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    static List<Field> dataFields(Object self) {
        return Arrays.stream(self.getClass().getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .collect(Collectors.toList());

    }

    static Annotation[] annotations(Object self, String name) {
        try {
            return Arrays.stream(self.getClass().getField(name).getAnnotations())
                    .filter(a -> a.annotationType().getAnnotation(Argument.class) != null).toArray(Annotation[]::new);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(format("Field %s does not exist in enum %s", name, self.getClass()));
        }
    }

    static <T> T create(Annotation a, Class<?> type) {
        return createValueData(a).createInstance(type);
        //return new FactoryCreator<T>().apply(d).apply(d.factoryMethodArguments());
    }

    private static ValueData createValueData(Annotation a) {
        return a instanceof Value ? createValueData((Value)a) : createValueDataFromStereotype(a);
    }


    private static ValueData createValueData(Value v) {
        return new ValueData(v.name(), v.value(), v.type(), v.factory(), v.factoryMethod(), new Object[0], new Object[0]); //, v.factoryMethod().isEmpty() ?  new Object[0] : new Object[] {v.value()});
    }


    private static ValueData createValueDataFromStereotype(Annotation a) {
        //Value v = a instanceof Value ? (Value)a : a.annotationType().getAnnotation(Value.class);
        Value[] values = a instanceof Value ? new Value[] {(Value)a} : a.annotationType().getAnnotationsByType(Value.class);
        Value v = values.length > 0 ? values[0] : null;

        final String name;
        final Object value;
        final Class type;
        final Class factory;
        final Object[] factoryArguments;
        final Object[] factoryMethodArguments;
        final String factoryMethod;
        if (v == null) {
            name = get(a, a, "name");
            Method valueMethod = method(a.annotationType(), "value");
            Class<?> valueReturnType = valueMethod.getReturnType();
            value = invoke(a, valueMethod);

            type = valueReturnType.isArray() ? valueReturnType.getComponentType() : valueReturnType;
            factory = type;
            factoryArguments = new Object[0];
            factoryMethodArguments = new Object[0];
            factoryMethod = "";
        } else {
            name = get(v, a, "name");
            value = get(v, a, "value");
            factory = v.factory();
            factoryArguments = Arrays.stream(values).filter(v1 -> v1.name().equals("factoryArgument"))
                    .map(p -> p.value()[0])
                    .map(arg -> deref(arg, a)).toArray(Object[]::new);

            factoryMethod = v.factoryMethod();
            String[] factoryMethodStringArguments = Arrays.stream(a.annotationType().getAnnotationsByType(Value.class))
                    .filter(v1 -> v1.name().equals("factoryMethodArgument"))
                    .map(p -> p.value()[0]).toArray(String[]::new);
            if (!v.factoryMethod().isEmpty() && factoryMethodStringArguments.length == 0) {
                factoryMethodStringArguments = v.value().length > 0 ? v.value() : new String[]{"${value}"};
            }

            factoryMethodArguments = Arrays.stream(factoryMethodStringArguments).map(arg -> deref(arg, a)).toArray();
            type = v.type();
        }

        return new ValueData(name, value, type, factory, factoryMethod, factoryArguments, factoryMethodArguments);
    }



    private static  <T> T get(Annotation meta, Annotation a, String method) {
        T res = invoke(meta, method(meta.annotationType(), method));
        return res.getClass().isArray() ? deref(((String[])res)[0], a) : deref((String)res, a);
    }


    @SuppressWarnings("unchecked")
    private static <T> T deref(String metaValue, Annotation a) {
        if (metaValue.startsWith("${") && metaValue.endsWith("}")) {
            String method = metaValue.substring(2, metaValue.length() - 1);
            Object dereferenced = invoke(a, method(a.annotationType(), method)); //TODO: find @Value annotation (factory method or factory argument) with value=${metaValue} and call createValueDataFromStereotype for it
            return (T)dereferenced;
        }
        return (T)metaValue;
    }



    static Method method(Class<?> clazz, String name) {
        try {
            return clazz.getMethod(name);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    static <T> T invoke(Object obj, Method method, Object... args) {
        try {
            method.setAccessible(true);
            return (T)method.invoke(obj, args);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    static boolean isContainer(Annotation a) {
        Method value;
        try {
            value = a.annotationType().getMethod("value");
        } catch (NoSuchMethodException e) {
            return false; // bad practice, but so short...
        }
        Class returnType = value.getReturnType();
        return returnType.isArray() && returnType.getComponentType().isAnnotation() &&
                Arrays.stream(returnType.getComponentType().getAnnotations()).anyMatch(a2 -> Argument.class.equals(a2.annotationType()));

    }
}
