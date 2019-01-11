package org.enumus.initializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Argument
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DoubleValue {
    String name();
    double[] value();
}
