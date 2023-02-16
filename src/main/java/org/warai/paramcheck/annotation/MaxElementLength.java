package org.warai.paramcheck.annotation;



import org.warai.paramcheck.constant.ErrorMessage;

import java.lang.annotation.*;

/**
 * @Auther: わらい
 * @Time: 2021/9/28 14:53
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MaxElementLength {

    int value();

    String msg() default ErrorMessage.ELEMENT_OVER_MAX_LENGTH;

    boolean nullable() default false;

    String[] groups() default {};

}
