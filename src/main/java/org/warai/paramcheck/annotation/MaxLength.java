package org.warai.paramcheck.annotation;

import org.warai.paramcheck.constant.ErrorMessage;

import java.lang.annotation.*;

/**
 * @Auther: わらい
 * @Time: 2021/9/26 14:07
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MaxLength {

    int value();

    String msg() default ErrorMessage.OVER_MAX_LENGTH;

    boolean nullable() default false;

    String[] groups() default {};
}
