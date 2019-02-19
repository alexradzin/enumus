package org.enumus.samples.article.enumwitannotations;


import org.enumus.initializer.Argument;
import org.enumus.initializer.Value;
import org.enumus.samples.article.IsoAlpha2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD})
@Value(name = "${name}", value = "${iso}", type = IsoAlpha2.class, factory = IsoAlpha2.class)
@Argument
public @interface IsoCountryCode {
    String name();
    IsoAlpha2 iso();
}
