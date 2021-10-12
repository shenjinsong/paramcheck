package org.warai.paramcheck.annotation;

import org.warai.paramcheck.constant.ErrorMessage;
import java.lang.annotation.*;

/**
 * @Auther: わらい
 * @Time: 2021/9/29 15:15
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MinValue {

    int value();

    String msg() default ErrorMessage.BELOW_MIN_VALUE;

    boolean nullable() default false;

    String[] groups() default {};
}
