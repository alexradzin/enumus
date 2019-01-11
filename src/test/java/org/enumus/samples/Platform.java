package org.enumus.samples;

import org.enumus.initializer.Argument;
import org.enumus.initializer.Value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
//@Value(name = "${name}", value = "${os}", type = OsType.class, factory = OsType.class, factoryMethod = "valueOf")
@Value(name = "${name}", value = "${os}", type = OsType.class, factory = OsType.class)
//@Value(name = "factoryMethodArgument", value = "${os}")
@Argument
public @interface Platform {
    String name();
    OsType[] os();
}
