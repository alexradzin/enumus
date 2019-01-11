package org.enumus.initializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;

@Argument
@Value(name = "${name}", value = {"${regex}", "${options}"}, type = Pattern.class, factory = Pattern.class, factoryMethod = "compile")
@Value(name = "factoryMethodArgument", value = "${regex}")
@Value(name = "factoryMethodArgument", value = "${options}", type = Integer.class, factory = RegexOptionsParser.class, factoryMethod = "parse")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PatternValue {
    String name();
    String regex();
    int options() default 0;
//    String options() default NO_OPTIONS;

    public static final String NO_OPTIONS = "$$NULL$$";
}
