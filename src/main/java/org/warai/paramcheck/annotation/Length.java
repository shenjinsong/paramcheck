package org.warai.paramcheck.annotation;

import org.warai.paramcheck.constant.ErrorMessage;
import java.lang.annotation.*;

/**
 * @Auther: わらい
 * @Time: 2021/9/29 13:11
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Length {

    int min() default 1;

    int max() default 256;

    boolean nullable() default false;

    String msg() default ErrorMessage.PARAM_ERROR;

    String[] groups() default {};

}
