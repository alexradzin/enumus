package org.enumus.initializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Argument
@Value(name = "${name}", value = {"${regex}", "${options}"}, type = Pattern.class, factory = Pattern.class, factoryMethod = "compile")
@Value(name = "factoryMethodArgument", value = "${regex}")
@Value(name = "factoryMethodArgument", value = "${options}")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PatternValue {
    String name();
    String regex();
    int options() default 0;
}
