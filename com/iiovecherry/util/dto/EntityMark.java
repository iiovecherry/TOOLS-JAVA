package com.iiovecherry.util.dto;

import java.lang.annotation.Documented;
import java.lang.annotation.*;
/**
 * 
 * @author D.I.hunter
 * @ClassName: EntityMark.java
 * @date 2014-12-11
 * @Description:the annotation to mark up entity information
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD,ElementType.TYPE})
public @interface EntityMark {
    /**
     * Get entity field name,if no value default the same name with DTO
     * if use type please get the class name in here
     * @return
     */
    String name() default "null";
    /**
     * Mark up the field can be update by DTO
     * @return
     */
    boolean updatable() default true;
    /**
     * Mark up the field can be create by DTO
     * @return
     */
    boolean creatable() default true;
    /**
     * Mark up the field show in DTO
     * @return
     */
    boolean showable() default true;
    /**
     * Mark up the filed is id;
     * @return
     */
    boolean isId() default false;
    /**
     * Date-time  Date-date
     * Mark up the field show type example (date to time or date )
     * @return
     */
    String showType() default "null";
    
}
