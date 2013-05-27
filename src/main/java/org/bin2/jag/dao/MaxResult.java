package org.bin2.jag.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annoted parameter will be used to set the MaxResult of the query
 * 
 * @see org.hibernate.Query#setFirstResult
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface MaxResult {

}
