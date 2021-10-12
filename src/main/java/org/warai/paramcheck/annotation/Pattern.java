package org.warai.paramcheck.annotation;

import org.warai.paramcheck.constant.ErrorMessage;
import java.lang.annotation.*;

/**
 * @Auther: わらい
 * @Time: 2021/9/29 11:52
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Pattern {

    String value();

    String msg() default ErrorMessage.FORMAT_NOT_MATCH;

    boolean nullable() default false;

    String[] groups() default {};
}
