package org.warai.paramcheck.annotation;

import org.warai.paramcheck.constant.ErrorMessage;
import java.lang.annotation.*;

/**
 * @Auther: わらい
 * @Time: 2021/9/26 13:55
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MaxValue {

    int value();

    String msg() default ErrorMessage.OVER_MAX_VALUE;

    boolean nullable() default false;

    String[] groups() default {};

    int decimalPlaces() default -1;
}
