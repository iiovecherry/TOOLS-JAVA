package com.iiovecherry.util.dto;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 
 * @author D.I.hunter
 * @ClassName: EntitySearch.java
 * @date 2014-12-11
 * @Description:annotation to Criteria search
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD})
public @interface EntitySearch {
    /**
     * Get search key default null get DTO's name
     * @return
     */
    String name() default "null";
    /**
     * Get match type
     *  0 accurate 
     * 1 great 11 great and equal 
     * 2 litter  21 litter and equal  
     * 3 blur 31 left blur 32 right blur
     * @return
     */
    int searchType() default 0;
    /**
     * Date-time  Date-date  Date-max Date-min  int
     * 
     * Mark up the value need to change to other type
     * @return
     */
    String dataType() default "null";
}
