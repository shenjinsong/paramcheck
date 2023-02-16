package org.warai.paramcheck.annotation;

import org.warai.paramcheck.constant.ErrorMessage;

import java.lang.annotation.*;

/**
 * @Auther: わらい
 * @Time: 2021/9/29 11:47
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Future {

    int value() default 0;

    String msg() default ErrorMessage.DATE_IS_FUTURE;

    String[] groups() default {};

    String format() default "";

    boolean nullable() default false;
}
