package org.warai.paramcheck.annotation;

import java.lang.annotation.*;

/**
 * @author 大叔
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ParamCheck {

    String[] value() default {};

    String errorCode() default  "400154";

    String msg() default "Bad request parameter";

}
