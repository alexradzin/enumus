package org.enumus.initializer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

@Argument
@Repeatable(EpochValues.class)
@Value(name = "${name}", value = "${value}", type = Date.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EpochValue {
    String name();
    long value();
}
