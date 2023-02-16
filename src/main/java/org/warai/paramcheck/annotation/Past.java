package org.warai.paramcheck.annotation;

import org.warai.paramcheck.constant.ErrorMessage;
import java.lang.annotation.*;

/**
 * @Auther: わらい
 * @Time: 2021/9/29 10:22
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Past {

    int value() default 0;

    String msg() default ErrorMessage.DATE_IS_PAST;

    String[] groups() default {};

    String format() default "";

    boolean nullable() default false;

}
