package org.bin2.jag.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* This annotation is used to specify the name of the corresponding named parameter in the query
* @see org.bin2.jag.dao.Dao
**/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface IndexParameter {
    /**
    * the name of the parameter in the query
    **/
    int value();
}
