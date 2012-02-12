package org.bin2.jag.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to specify the name of the namedQuery used for a method on a DAO
 *
 * @see Dao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NamedQuery {
    /**
     * @return the name of the namedQuery
     */
    String value() default "";
}

