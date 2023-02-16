package org.warai.paramcheck.annotation;

import org.warai.paramcheck.constant.ErrorMessage;

import java.lang.annotation.*;

/**
 * @Auther: わらい
 * @Time: 2021/10/28 10:54
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValueSet {

    String[] value();

    String msg() default ErrorMessage.PARAM_ERROR;

    boolean nullable() default false;

    String[] groups() default {};
}
