package de.hochschuletrier.gdw.commons.jackson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to help parse Json maps
 *
 * @author Santo Pfingsten
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JacksonMap {

    Class<?> value();
}
