package org.enumus.initializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Argument
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Repeatable(Values.class)
public @interface Value {
    String name();
    String[] value();
    Class type() default String.class;
    Class<?> factory() default String.class;
    String factoryMethod() default ""; // default for ConstructorFactory that implements Function

}
