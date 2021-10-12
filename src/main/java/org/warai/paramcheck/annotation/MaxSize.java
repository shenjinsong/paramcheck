package org.warai.paramcheck.annotation;

import org.warai.paramcheck.constant.ErrorMessage;

import java.lang.annotation.*;

/**
 * @Auther: わらい
 * @Time: 2021/9/24 16:27
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MaxSize {

    int value();

    String msg() default ErrorMessage.OVER_MAX_ELEMENT_SIZE;

    boolean nullable() default false;

    String[] groups() default {};
}
