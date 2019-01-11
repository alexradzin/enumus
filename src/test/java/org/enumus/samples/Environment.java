package org.enumus.samples;

import org.enumus.initializer.Argument;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Argument
public @interface Environment {
    String name();
    RuntimeEnvironment[] value();
}
