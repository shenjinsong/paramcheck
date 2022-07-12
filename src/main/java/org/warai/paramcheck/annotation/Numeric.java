package org.warai.paramcheck.annotation;

import org.warai.paramcheck.constant.ErrorMessage;

import java.lang.annotation.*;

/**
 * @Auther: わらい
 * @Time: 2022/7/12 16:08
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Numeric {

    String msg() default ErrorMessage.FIELD_TYPES_NOT_MATCH;

    int decimalPlaces() default -1;

    boolean nullable() default false;

    String[] groups() default {};
}
