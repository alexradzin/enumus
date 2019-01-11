package org.enumus.initializer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public interface Initializable {
//    StringBuilder currentMember = new StringBuilder();
//    AtomicInteger fieldCount = new AtomicInteger(0);
//    List<Field> fields = new ArrayList<>();

    Map<Class, Object> defaultValues = new HashMap<Class, Object>() {{
        put(int.class, 0);
        put(long.class, 0);
        put(short.class, 0);
        put(byte.class, (byte)0);
        put(float.class, 0.0F);
        put(double.class, 0.0);
        put(boolean.class, false);
        put(char.class, (char)0);
    }};



    Map<Class, ClassData> initializationContext = new HashMap<>();
    //ClassData context = new ClassData();

    class ClassData {
        StringBuilder currentMember = new StringBuilder();
        AtomicInteger fieldCount = new AtomicInteger(0);
        List<Field> fields = new ArrayList<>();
    }


    default void init() {
        System.out.println(this);
        System.out.println(getClass());

        String name = toString();
        Class clazz = getClass();

//        System.out.println(this.getClass().getDeclaringClass());
//        System.out.println(getClass().getEnumConstants());
//        System.out.println(Arrays.asList(getClass().getEnumConstants()));

        try {
            System.out.println(Arrays.toString(clazz.getField(name).getAnnotations()));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    default <T> T $() {
        return $(null);
    }


    default <T> T $(T defaultValue) {
        String name = Util.name(this);


        Class clazz = getClass();
        if (!clazz.isEnum()) {
            clazz = clazz.getSuperclass();
            if (!clazz.isEnum()) {
                throw new IllegalStateException("This is not an enum");
            }
        }
        ClassData context = initializationContext.getOrDefault(clazz, new ClassData());
        initializationContext.putIfAbsent(clazz, context);


        if (context.fields.isEmpty()) {
            context.fields.addAll(Util.dataFields(this));
        }


        if (!context.currentMember.toString().equals(name)) {
            context.currentMember.setLength(0);
            context.currentMember.append(name);
            context.fieldCount.set(0);
        }
        Annotation[] annotations = Util.annotations(this, name);
        // TODO create plain list of single annotations hidden into containers (Values -> [Value], Platforms -> [Platform] etc)
        System.out.println(clazz + "#" + name + "#" + context.fieldCount + ": " + Arrays.toString(annotations));

        Field param = Arrays.stream(clazz.getDeclaredFields()).filter(f -> !Modifier.isStatic(f.getModifiers())).collect(Collectors.toList()).get(context.fieldCount.get()); //[fieldCount.get()];
        String paramName = param.getName();

        System.out.println(param.getType());

        @SuppressWarnings("unchecked")
        T value = (T)Arrays.stream(annotations)
                .map(a -> Util.isContainer(a) ? Util.invoke(a, Util.method(a.annotationType(), "value")) : new Annotation[] {a})
                .flatMap(Arrays::stream)
                .filter(a -> a.annotationType().getAnnotation(Argument.class) != null)
                .filter(a -> Util.name(a).equals(paramName))
                .findFirst()
                .map(Util::create)
                .orElseGet(() -> defaultValue == null ? defaultValues.get(param.getType()) : defaultValue);
                //.orElse(defaultValue);

        context.fieldCount.incrementAndGet();
        return value;
    }



    //    default <T> T $(T defaultValue) {
//        String name = toString();
//        Class clazz = getClass();
//        System.out.println(clazz + "#" + name);
//        return defaultValue;
//    }
}
