package org.warai.paramcheck.annotation;

import org.warai.paramcheck.constant.ErrorMessage;

import java.lang.annotation.*;

/**
 * @Auther: わらい
 * @Time: 2021/9/29 14:54
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ElementLength {

    int min() default 1;

    int max() default 256;

    boolean nullable() default false;

    String msg() default ErrorMessage.CHARACTER_LENGTH_NOT_MATCH;

    String[] groups() default {};
}
